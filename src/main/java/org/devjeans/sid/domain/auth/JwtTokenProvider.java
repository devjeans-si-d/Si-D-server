package org.devjeans.sid.domain.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;

    public String createToken(String id, String role) {
//        Claims는 사용자 정보(페이로드 정보)
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간           분 초 밀리초
                .setExpiration(new Date(now.getTime()+expiration*60*1000L)) // 만료시간 30분
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        return token;
    }
}