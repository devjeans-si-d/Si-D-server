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
        log.info("validateWebSocketToken() 진입! 웹소켓 연결 시 헤더의 토큰 유효성 검증 시작!");
        try {
            // BEARER 뒤의 실제 토큰 데이터만 남긴다.
            log.info("line 68. 토큰 검증 {}", bearerToken);
            String token = bearerToken.replace("Bearer ", "");

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new BaseException(INVALID_TOKEN);
        }
    }
}