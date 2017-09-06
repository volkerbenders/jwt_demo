package com.uum;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class JWTGenerator {

        public static final String SECRET = "secret";
        public static final String AUTH_0 = "auth0";
        public static final String CLAIM_DATA = "data";

        public String generate(String testData) {
                String token ="";
                try {
                        Algorithm algorithm = Algorithm.HMAC256(SECRET);
                        token = JWT.create()
                                .withExpiresAt(getTimeStamp())
                                .withClaim(CLAIM_DATA, testData)
                                .withIssuer(AUTH_0)
                                .sign(algorithm);
                } catch (UnsupportedEncodingException exception){
                        //UTF-8 encoding not supported
                }

                return token;
        }


        private Date getTimeStamp() {
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.MINUTE, 5);
                return instance.getTime();
        }

        public String decodeToken(String token) {
                String claimUserId = null;
                try {
                        JWTVerifier verifier = buildJwtVerifier();
                        DecodedJWT jwt = verifier.verify(token);
                        claimUserId = jwt.getClaim(CLAIM_DATA).asString();
                } catch (UnsupportedEncodingException exception) {
                        System.err.println("UnsupportedEncodingException: " + exception);
                }
                return claimUserId;
        }

        private JWTVerifier buildJwtVerifier() throws UnsupportedEncodingException {
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                return JWT.require(algorithm)
                        .withIssuer(AUTH_0)
                        .build();
        }
}
