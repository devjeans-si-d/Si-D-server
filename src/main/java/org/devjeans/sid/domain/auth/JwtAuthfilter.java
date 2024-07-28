package org.devjeans.sid.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtAuthfilter extends GenericFilter {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)request).getHeader("Authorization");
//        try{
            if(bearerToken!=null){
//            token은 관례적으로 Bearer 로 시작하는 문구를 넣어서 요청
                if(!bearerToken.substring(0,7).equals("Bearer ")){
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다");
                }
                String token = bearerToken.substring(7);
//            token 검증 및 claims(사용자정보) 추출
//            token생성시에 사용한 secret키값을 넣어 토큰 검증에 사용
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody(); // 다시 암호화해서 토큰검증과 동시에 사용자 정보 페이로드 입력        }else {

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
//            authentication객체 만들기 위해 userDetails 필요
                UserDetails userDetails = new User(claims.getSubject(),"",authorities); //getSubject 에 유저정보
//            Authentication객체생성(UserDetails객체도 필요) : 스프링 전역 객체 에 인증 정보를 담는다
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
//        filterchain에서 그 다음 filtering로 넘어가도록하는 메서드
            chain.doFilter(request,response);
//        }catch (Exception e){
//            log.error(e.getMessage(),e);
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            httpServletResponse.setContentType("application/json;charset=utf-8");
//            httpServletResponse.getWriter().write("token에러");
//        }
    }
}