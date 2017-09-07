package com.uum;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTService {

    public static final String SECRET = "secret";
    public static final String ISSUER = "auth0";
    public static final int DEFAULT_TOKEN_TTL_IN_MINUTES = 5;

    /**
     * Generate a JWT-Token for the given String. Default tokens a valid for 5 Minutes.
     *  Transfer data is stored with the tokens subject as described here
     *  https://stackoverflow.com/questions/32152574/where-to-store-user-id-in-jwt
     *
     * @param dataToTransfer
     * @return encrypted Base64 encoded String
     * @throws UnsupportedEncodingException
     */
    public String generateJwt(String dataToTransfer) throws UnsupportedEncodingException {
        String token = "";
            Algorithm algorithm = getAlgorithm();
            token = JWT.create()
                    .withExpiresAt(getTimeStamp())
                    .withSubject(dataToTransfer)
                    .withIssuer(ISSUER)
                    .sign(algorithm);


        return token;
    }

    /**
     * Decode the given JWT.
     * @param token
     * @return Base64Decoded and decrypted string
     */
    public String decodeJwt(String token) {
        String claimUserId = null;
        try {
            DecodedJWT jwt = verifyToken(token);
            claimUserId = jwt.getSubject();
        } catch (UnsupportedEncodingException exception) {
            System.err.println("UnsupportedEncodingException: " + exception);
        }
        return claimUserId;
    }

    public DecodedJWT verifyToken(String token) throws UnsupportedEncodingException {
        return buildJwtVerifier().verify(token);
    }

    private JWTVerifier buildJwtVerifier() throws UnsupportedEncodingException {
        Algorithm algorithm = getAlgorithm();
        return JWT.require(algorithm).withIssuer(ISSUER).build();
    }

    private Algorithm getAlgorithm() throws UnsupportedEncodingException {
        return Algorithm.HMAC256(SECRET);
    }

    private Date getTimeStamp() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, DEFAULT_TOKEN_TTL_IN_MINUTES);
        return instance.getTime();
    }
}
