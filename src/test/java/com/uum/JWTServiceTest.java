package com.uum;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;

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
    public void createToken() {
        String testData = "Ich bin ein lesbarere String";
        String token = jwtService.generate(testData);
        assertThat(token).isNotEmpty();
        int count = StringUtils.countMatches(token, '.');
        assertThat(count).isEqualTo(2);
    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_justOnePart() {
        jwtService.decodeToken("itzelBritzel");
        System.out.println("JWTServiceTest.decodeToken: token:\n");// + token);

    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_twoParts() {
        jwtService.decodeToken("itzelBritzel.trallala");
        System.out.println("JWTServiceTest.decodeToken: token:\n");// + token);
    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_tooManyParts() {
        jwtService.decodeToken("itzelBritzel.trallala..bla.fasel.blubb");
        System.out.println("JWTServiceTest.decodeToken: token:\n");// + token);
    }

    @Test(expected = TokenExpiredException.class)
    public void verifyToken_validButExpiredToken() {
        jwtService.decodeToken(testTokenWithTimeStamp);
    }

    @Test
    public void encodeDecode() {
        String testData = "Ich bin ein lesbarere String";
        String jwtToken = jwtService.generate(testData);

        String decoded = jwtService.decodeToken(jwtToken);
        assertThat(testData).isEqualTo(decoded);
    }

    @Test
    public void encodeDecodeUserId() {
        String sourceUserId = "xne1399";
        String jwtToken = jwtService.generate(sourceUserId);

        String decodedUserId = jwtService.decodeToken(jwtToken);
        assertThat(sourceUserId).isEqualTo(decodedUserId);
        System.out.println("JWTServiceTest.encodeDecodeUserId: encoded '"+sourceUserId+"' is same as decoded '"+decodedUserId+"'");
    }
}
