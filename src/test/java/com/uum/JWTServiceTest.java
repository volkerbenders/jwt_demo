package com.uum;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import java.io.UnsupportedEncodingException;

public class JWTServiceTest {
    JWTService jwtService;
    String testTokenWithTimeStamp =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTUwNDYyNzU3OX0.-XvTHUn8ib-bABVXzwGgY5PUXO7h7uhY4wRgvsUF9dY";

    @Before
    public void init() {
        jwtService = new JWTService();
    }

    @Test
    public void test1() {
        assertNotEquals("a", "b");
    }

    @Test
    public void createToken() throws UnsupportedEncodingException {
        String testData = "Ich bin ein lesbarere String";
        String token = jwtService.generateJwt(testData);
        assertThat(token).isNotEmpty();
        int count = StringUtils.countMatches(token, '.');
        assertThat(count).isEqualTo(2);
    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_justOnePart() {
        jwtService.decodeJwt("itzelBritzel");
        System.out.println("JWTServiceTest.decodeJwt: token:\n");// + token);

    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_twoParts() {
        jwtService.decodeJwt("itzelBritzel.trallala");
        System.out.println("JWTServiceTest.decodeJwt: token:\n");// + token);
    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_tooManyParts() {
        jwtService.decodeJwt("itzelBritzel.trallala..bla.fasel.blubb");
        System.out.println("JWTServiceTest.decodeJwt: token:\n");// + token);
    }

    @Test(expected = TokenExpiredException.class)
    public void verifyToken_validButExpiredToken() {
        jwtService.decodeJwt(testTokenWithTimeStamp);
    }

    @Test
    public void encodeDecode() throws UnsupportedEncodingException {
        String testData = "Ich bin ein lesbarere String";
        String jwtToken = jwtService.generateJwt(testData);

        String decoded = jwtService.decodeJwt(jwtToken);
        assertThat(testData).isEqualTo(decoded);
    }

    @Test
    public void encodeDecodeUserId() throws UnsupportedEncodingException {
        String sourceUserId = "xne1399";
        String jwtToken = jwtService.generateJwt(sourceUserId);

        String decodedUserId = jwtService.decodeJwt(jwtToken);
        assertThat(sourceUserId).isEqualTo(decodedUserId);
        System.out.println("JWTServiceTest.encodeDecodeUserId: encoded '"+sourceUserId+"' is same as decoded '"+decodedUserId+"'");
    }
}
