package com.sky.yb.user.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {
    private Key key;
    @Mock
    private UserDetails userDetails;
    @Mock
    private HttpServletRequest request;

    private JwtTokenUtil target;

    @BeforeEach
    void setUp() {
        String secret = "testsecretkeytestsecretkeytestsecretkeytestsecretkey";
        long jwtExpirationInMs = 3600000;
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        target = new JwtTokenUtil(key);
        target.setJwtExpirationInMs(jwtExpirationInMs);
    }

    @Test
    public void withValidToken_onGetEmailFromToken_shouldReturnEmail() {
        // given
        String expectedEmail = "test@example.com";

        Claims claims = Jwts.claims().setSubject(expectedEmail);
        String compactToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // when
        String email = target.getEmailFromToken(compactToken);

        // then
        assertEquals(expectedEmail, email);
    }

    @Test
    public void withInvalidToken_onGetEmailFromToken_shouldReturnEmpty() {
        // given
        String token = "invalidToken";

        // when
        String email = target.getEmailFromToken(token);

        // then
        assertEquals("", email);
    }

    @Test
    public void withValidToken_onGenerateToken_shouldReturnToken() {
        // given
        String subject = "test@example.com";

        when(userDetails.getUsername()).thenReturn(subject);

        // when
        String token = target.generateToken(userDetails);

        // then
        assertNotNull(token);
    }

    @Test
    public void withValidToken_onIsTokenValid_shouldReturnTrue() {
        // given
        String username = "test@example.com";

        Map<String, Object> claims = new HashMap<>();

        String compactToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        when(userDetails.getUsername()).thenReturn(username);

        // when
        Boolean isValid = target.isTokenValid(compactToken, username, userDetails);

        // then
        assertTrue(isValid);
    }

    @Test
    public void withInvalidToken_onIsTokenValid_shouldReturnFalse() {
        // given
        String token = "invalidToken";
        String username = "test@example.com";

        // when
        Boolean isValid = target.isTokenValid(token, username, userDetails);

        // then
        assertFalse(isValid);
    }

    @Test
    public void withBearerTokenHeader_onRetrieveJwtFromRequest_shouldReturnToken() {
        // given
        String bearerToken = "Bearer validToken";

        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        // when
        String token = target.retrieveJwtFromRequest(request);

        // then
        assertEquals("validToken", token);
    }

    @Test
    public void withNonBearerTokenHeader_onRetrieveJwtFromRequest_shouldReturnToken() {
        // given
        String nonBearerToken = "NonBearerToken";

        when(request.getHeader("Authorization")).thenReturn(nonBearerToken);

        // when
        String token = target.retrieveJwtFromRequest(request);

        // then
        assertEquals(nonBearerToken, token);
    }

    @Test
    public void withNullToken_onRetrieveJwtFromRequest_shouldReturnEmpty() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        String token = target.retrieveJwtFromRequest(request);

        // then
        assertEquals("", token);
    }

}
