package com.hu.brg.service.controller;

import com.hu.brg.service.ConfigSelector;
import com.hu.brg.service.model.required.DBEngine;
import com.hu.brg.service.model.required.Project;
import com.hu.brg.service.model.web.ErrorResponse;
import com.hu.brg.service.persistance.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.service.persistance.tooldatabase.ProjectDAO;
import com.hu.brg.service.persistance.tooldatabase.ProjectDAOImpl;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class AuthController {

    private AuthController() {
    }

    private static String secretKey = ConfigSelector.SECRET_KEY;

    @OpenApi(
            summary = "Create Connection",
            operationId = "createConnection",
            path = "/auth/connection",
            method = HttpMethod.POST,
            tags = {"Auth", "Connection"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void createConnection(io.javalin.http.Context context) {
        try {

            ProjectDAO projectDAO = new ProjectDAOImpl();

            JSONObject jsonObject = new JSONObject(context.body());
            if (jsonObject.length() != 7) {
                context.result("Can't create connection due to unfulfilled data requirements").status(400);
                return;
            }

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            Project project = projectDAO.getProjectByIdentifiers(jsonObject.getString("host"), Integer.parseInt(jsonObject.getString("port")),
                    jsonObject.getString("service"),
                    DBEngine.valueOf(jsonObject.getString("engine")),
                    jsonObject.getString("dbName"));

            if (project == null) {
                project = new Project(jsonObject.getString("host"), Integer.parseInt(jsonObject.getString("port")),
                        jsonObject.getString("service"),
                        DBEngine.valueOf(jsonObject.getString("engine")),
                        jsonObject.getString("dbName"));

                project = projectDAO.saveProject(project);
            }

            if (!new TargetDatabaseDAOImpl().testConnection(jsonObject.getString("username"), jsonObject.getString("password"), project)) {
                context.status(403);
                return;
            }

            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject("3dwC-782de-4e")
                    .setIssuer("hu-brg-opensource")
                    .claim("username", jsonObject.getString("username"))
                    .claim("password", jsonObject.getString("password"))
                    .claim("projectId", project.getId())
                    .signWith(signatureAlgorithm, signingKey);

            long expMillis = nowMillis + 3600000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);

            context.result(builder.compact()).status(200);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500);
        }

    }

    public static Claims decodeJWT(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
