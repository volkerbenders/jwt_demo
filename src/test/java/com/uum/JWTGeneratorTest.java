package com.uum;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JWTGeneratorTest {
    JWTGenerator jwtGenerator;
    String testTokenWithTimeStamp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTUwNDYyNzU3OX0.-XvTHUn8ib-bABVXzwGgY5PUXO7h7uhY4wRgvsUF9dY";

    @Before
    public void init() {
        jwtGenerator = new JWTGenerator();
    }

    @Test
    public void test1() {
        assertNotEquals("a", "b");
    }

    @Test
    public void createToken() {
        String testData = "Ich bin ein lesbarere String";
        String token = jwtGenerator.generate(testData);
        assertThat(token).isNotEmpty();
        int count = StringUtils.countMatches(token, '.');
        assertThat(count).isEqualTo(2);
    }


    @Test(expected = JWTDecodeException.class)
    public void verifyToken_justOnePart(){
        //String token = jwtGenerator.decodeToken(testTokenWithTimeStamp);
        jwtGenerator.decodeToken("itzelBritzel");
        System.out.println("JWTGeneratorTest.decodeToken: token:\n");// + token);

    }
    @Test(expected = JWTDecodeException.class)
    public void verifyToken_twoParts(){
        jwtGenerator.decodeToken("itzelBritzel.trallala");
        System.out.println("JWTGeneratorTest.decodeToken: token:\n");// + token);
    }

    @Test(expected = JWTDecodeException.class)
    public void verifyToken_tooManyParts(){
        jwtGenerator.decodeToken("itzelBritzel.trallala..bla.fasel.blubb");
        System.out.println("JWTGeneratorTest.decodeToken: token:\n");// + token);
    }
    @Test(expected = TokenExpiredException.class)
    public void verifyToken_validButExpiredToken(){
        jwtGenerator.decodeToken(testTokenWithTimeStamp);
    }

    @Test
    public void encodeDecode(){
        String testData = "Ich bin ein lesbarere String";
        String jwtToken = jwtGenerator.generate(testData);

        String decoded = jwtGenerator.decodeToken(jwtToken);
        System.out.println("JWTGeneratorTest.encodeDecode: decoded: " + decoded);
        assertThat(testData).isEqualTo(decoded);
    }
}
