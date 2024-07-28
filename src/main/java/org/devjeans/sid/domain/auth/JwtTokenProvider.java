package org.devjeans.sid.domain.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.devjeans.sid.global.exception.exceptionType.TokenException.INVALID_TOKEN;

@Slf4j
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
                .setIssuedAt(now) // 생성시간                일수 시간 분 초 밀리초
                .setExpiration(new Date(now.getTime()+expiration*24*60*60*1000L)) // 만료시간 30분
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        return token;
    }

    public void validateWebSocketToken(String bearerToken) {
        try {
            // log.info("line 40. 토큰 검증 {}", bearerToken);
            String token = bearerToken.replace("Bearer ", "");

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            // log.info("[line 48] 멤버 아이디: {}", claims.getSubject());
        } catch (Exception e) {
            throw new BaseException(INVALID_TOKEN);
        }
    }
}