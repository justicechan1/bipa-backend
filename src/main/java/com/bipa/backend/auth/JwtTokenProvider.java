package com.bipa.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final byte[] key;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtTokenProvider(AppJwtProps props){
        this.key = props.getSecret().getBytes();
        this.accessExpMs = props.getAccessExpSeconds() * 1000L;
        this.refreshExpMs = props.getRefreshExpSeconds() * 1000L;
    }

    public String createAccessToken(Long userId){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpMs))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpMs))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long parseUserId(String token){
        Claims c = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(c.getSubject());
    }
}
