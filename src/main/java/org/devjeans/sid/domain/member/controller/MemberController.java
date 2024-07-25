package org.devjeans.sid.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.member.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberInfoResponse memberInfo = memberService.getMemberInfo(memberId);

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

    @PostMapping("/{memberId}/update")
    public ResponseEntity<UpdateMemberResponse> getMemberInfo(
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRequest updateMemberRequest) {

        UpdateMemberResponse updateMemberResponse = memberService.updateMemberInfo(memberId, updateMemberRequest);

        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }


    @GetMapping("auth/kakao/callback")
    public String kakaoCallback(KakaoRedirect kakaoRedirect) throws JsonProcessingException {
        Long kakaoId = memberService.login(kakaoRedirect);
        System.out.println(kakaoId);

//            가입자 or 비가입자 체크해서 처리
        Member originMember = memberService.getMemberByKakaoId(kakaoId);
        System.out.println(originMember);
        if(originMember == null) {
//           신규 회원일경우 errorResponse에 소셜id를 담아 예외를 프론트로 던지기
//            프론트는 예외일경우 회원가입 화면으로 이동하여 회원가입 정보와 소셜id를 담아 다시 회원가입 요청
            memberService.createMember(kakaoId);
        }
//            로그인 처리
        return kakaoId.toString();
    }

    @GetMapping("register")
    public ResponseEntity<String> registerMember(@RequestBody RegisterMemberRequest dto) {
        memberService.registerMember(dto);
        return new ResponseEntity<>("register succes!!", HttpStatus.OK);
    }


}
