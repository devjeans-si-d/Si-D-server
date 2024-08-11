package org.devjeans.sid.domain.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.TokenException.INVALID_TOKEN;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;
    private static final Long expriationPeriod = 24*60*60*1000L;

    public String createToken(String id, String role) {
//        Claims는 사용자 정보(페이로드 정보)
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간                일수 시간 분 초 밀리초
                .setExpiration(new Date(now.getTime()+expiration*expriationPeriod)) // 만료시간 30분
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
        } catch (Exception e) {
            throw new BaseException(INVALID_TOKEN);
        }
    }

    public Long getMemberIdFromToken(String bearerToken) {
        try {
            // log.info("line 40. 토큰 검증 {}", bearerToken);
            String token = bearerToken.replace("Bearer ", "");

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new BaseException(INVALID_TOKEN);
        }
    }
}