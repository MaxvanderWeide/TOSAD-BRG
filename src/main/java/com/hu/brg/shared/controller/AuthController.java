package com.hu.brg.shared.controller;

import com.hu.brg.define.domain.DBEngine;
import com.hu.brg.define.domain.Project;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.ProjectDAO;
import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.web.ErrorResponse;
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
import java.util.Collections;
import java.util.Date;

public class AuthController {

    private AuthController() {}

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
        // TODO - Remove dependency from define
        try {
            ProjectDAO projects = DAOServiceProvider.getProjectDAO();
            JSONObject jsonObject = new JSONObject(context.body());
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            Project project = DAOServiceProvider.getProjectDAO().getProjectByIdentifiers(jsonObject.getString("host"), Integer.parseInt(jsonObject.getString("port")),
                    jsonObject.getString("service"),
                    DBEngine.valueOf(jsonObject.getString("engine")),
                    jsonObject.getString("dbName"));

            if (project == null) {
                project = new Project(jsonObject.getString("host"), Integer.parseInt(jsonObject.getString("port")),
                        jsonObject.getString("service"),
                        DBEngine.valueOf(jsonObject.getString("engine")),
                        jsonObject.getString("dbName"), Collections.emptyList());

                project = DAOServiceProvider.getProjectDAO().saveProject(project);
            }


            //TODO: Bart: this can be removed I think
//            project.setUsername(jsonObject.getString("username"));
//            project.setPassword(jsonObject.getString("password"));
//            int projectId = projects.getProjectById(project);
//            TargetDatabaseDAO targetDatabaseDAO = TargetDatabaseDAOImpl.createTargetDatabaseDAOImpl(
//                    DBEngine.ORACLE,
//                    project.getHost(),
//                    project.getPort(),
//                    project.getService(),
//                    jsonObject.getString("username"),
//                    jsonObject.getString("password")
//            );
//            if (!targetDatabaseDAO.testConnection()) {
//                context.status(403);
//                return;
//            }
//            if (projectId == 0) {
//                projects.saveProject(project);
//                projectId = project.getId();
//            }

            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject("3dwC-782de-4e")
                    .setIssuer("hu-brg-opensource")
                    .claim("engine", project.getDbEngine().name())
                    .claim("dbName", project.getName())
                    .claim("host", project.getHost())
                    .claim("service", project.getService())
                    .claim("username", jsonObject.getString("username"))
                    .claim("password", jsonObject.getString("password"))
                    .claim("projectId", project.getId())
                    .claim("port", project.getPort())
                    .signWith(signatureAlgorithm, signingKey);

            long expMillis = nowMillis + 3600000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);

            context.result(builder.compact()).status(200);
            if (jsonObject.length() != 7) {
                context.result("Can't create connection due to unfulfilled data requirements").status(400);
            }
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
