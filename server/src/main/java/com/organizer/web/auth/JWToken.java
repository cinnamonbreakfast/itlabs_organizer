package com.organizer.web.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;


public class JWToken {
    static String secret= "SUPER_SECRET_KEY";
    static Algorithm algorithm = Algorithm.HMAC256(secret);
    static long ttlMillis=86400 * 1000;
    public static String create(String mail){
        Date date = new Date(ttlMillis);
        try{
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("id",mail)
                    .withExpiresAt(date)
                    .sign(algorithm);
            return  token;
        }catch (Exception e){
            return null;
        }
    }
    public static String checkToken(String token){
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("id").asString();
        } catch (JWTVerificationException exception){
            return null;
        }
    }
}
