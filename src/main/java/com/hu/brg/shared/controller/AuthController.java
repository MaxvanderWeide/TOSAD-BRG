package com.hu.brg.shared.controller;

import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.model.web.ErrorResponse;
import com.hu.brg.shared.persistence.DBEngine;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.shared.persistence.tooldatabase.ProjectsDAO;
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

    private static String SECRET_KEY = ConfigSelector.SECRET_KEY;

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

            ProjectsDAO projects = DAOServiceProvider.getProjectsDAO();
            JSONObject jsonObject = new JSONObject(context.body());
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            Project project = new Project(DBEngine.valueOf(jsonObject.getString("engine")),
                    jsonObject.getString("dbName"),
                    jsonObject.getString("host"),
                    Integer.valueOf(jsonObject.getString("port")),
                    jsonObject.getString("service"));

            project.setUsername(jsonObject.getString("username"));
            project.setPassword(jsonObject.getString("password"));
            int projectId = projects.getProjectId(project); // TODO - Simplify this?
            if (projectId == 0) {
                projects.saveProject(project);
                projectId = project.getId();
            }

            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject("3dwC-782de-4e")
                    .setIssuer("hu-brg-opensource")
                    .claim("engine", project.getDbEngine().name())
                    .claim("dbName", project.getName())
                    .claim("host", project.getHost())
                    .claim("service", project.getServiceName())
                    .claim("password", project.getPassword())
                    .claim("username", project.getUsername())
                    .claim("projectId", projectId)
                    .claim("port", project.getPort())
                    .signWith(signatureAlgorithm, signingKey);

            long expMillis = nowMillis + 3600000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);

            context.result(builder.compact()).status(200);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
        }

    }

    public static Claims decodeJWT(String jwt) {
        // TODO - Move this decode to somewhere safer?
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
