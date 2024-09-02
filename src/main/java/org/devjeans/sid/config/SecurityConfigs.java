package org.devjeans.sid.config;

import org.devjeans.sid.domain.auth.JwtAuthfilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity //시큐리티 코드라고 선언해 알려주는 코드
@EnableGlobalMethodSecurity(prePostEnabled = true) // pre : 사전검증, post : 사후검증
public class SecurityConfigs extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthfilter jwtAuthfilter;
    //    모든필터가 모여있는 필터 체인
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//  CORS 다른 도메인에서 서버로 호출하는 것을 금지
//        같은 도메인 끼리면 허용
//        localhost:8080 localhost:8081 => 다른 도메인
//        antMatcher("/","member/create","doLogin")
        return httpSecurity
                .csrf().disable()
                .cors().and() //CORS활성화 다른 도메인에서 서버로 호출하는 것을 금지
                .httpBasic().disable() // 관례적으로 들어감
                .authorizeRequests()
                .antMatchers("/",
                        "/api/auth/register",
                        "https://kauth.kakao.com/oauth/authorize",
                        "/api/auth/kakao/callback",
                        "/api/auth/login",
                        "/chat/**",
                        "/api/launched-project/list",
                        "/api/main/top-launched-project",
                        "/api/main/top-sider-card",
                        "/api/main/top-project",
                        "/api/launched-project/detail/{id}/basic-info",
                        "/api/launched-project/detail/{id}/tech-stacks",
                        "/api/launched-project/detail/{id}/members",
                        "/api/project/listAll",
                        "/api/project/{id}",
                        "/api/project/{id}/isScrap",
                        "/api/sider-card/list")
                .permitAll()
                .anyRequest().authenticated()
                .and()
//                세션로그인이 아닌 stateless한 token을 사용하겠다라는 의미
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                로그인시 사용자는 서버로부터 토큰을 받급받고
//                매요청마다 해당토큰을 http header에 넣어 요청
//                사용자로부터 받아온 토큰이 정상인지 아닌지를 검증하는 코드
                .addFilterBefore(jwtAuthfilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://dev.si-d.site", "http://localhost:8082")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
}