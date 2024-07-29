package org.devjeans.sid.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.auth.JwtTokenProvider;
import org.devjeans.sid.domain.auth.service.AuthService;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.auth.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
// RequiredArgsConstructor : AutoWired 생성자 주입이 딱히 필요없고
// final 이 붙은 bean객체들을 자동으로 주입해준다
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Value("${jwt.secretKey}")
    private String secretKey;

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(KakaoRedirect kakaoRedirect) throws JsonProcessingException {
        Long kakaoId = authService.login(kakaoRedirect);
        System.out.println(kakaoId);

//            가입자 or 비가입자 체크해서 처리
        Member originMember = authService.getMemberByKakaoId(kakaoId);
        System.out.println(originMember);
        if(originMember == null) {
//           신규 회원일경우 errorResponse에 소셜id를 담아 예외를 프론트로 던지기
//            프론트는 예외일경우 회원가입 화면으로 이동하여 회원가입 정보와 소셜id를 담아 다시 회원가입 요청
            return new ResponseEntity<>(kakaoId,HttpStatus.UNAUTHORIZED);
        }
        String jwtToken = jwtTokenProvider.createToken(String.valueOf(originMember.getId()),originMember.getRole().toString());
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id",originMember.getId());
        loginInfo.put("token",jwtToken);
//            로그인 처리
        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }

    @GetMapping("register")
    public ResponseEntity<String> registerMember(@RequestBody RegisterMemberRequest dto) {
        authService.registerMember(dto);
        return new ResponseEntity<>("register succes!!", HttpStatus.OK);
    }

    @GetMapping("delete")
    public ResponseEntity<String> deleteMember() {
//        String tmp = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = authService.delete();
        return new ResponseEntity<>(member.getDeletedAt().toString(), HttpStatus.OK);
    }

}
