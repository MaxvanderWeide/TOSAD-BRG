package com.hu.brg.shared.controller;

import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.response.ErrorResponse;
import com.sun.xml.internal.org.jvnet.mimepull.DecodingException;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import org.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;

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

            JSONObject jsonObject = new JSONObject(context.body());
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject("3dwC-782de-4e")
                    .setIssuer("hu-brg-opensource")
                    .claim("engine", jsonObject.getString("engine"))
                    .claim("dbName", jsonObject.getString("dbName"))
                    .claim("host", jsonObject.getString("host"))
                    .claim("service", jsonObject.getString("service"))
                    .claim("username", jsonObject.getString("username"))
                    .claim("password", jsonObject.getString("password"))
                    .claim("port", Integer.valueOf(jsonObject.getString("port")))
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
