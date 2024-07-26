package org.devjeans.sid.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.*;
import org.devjeans.sid.domain.member.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.service.MemberService;
import org.devjeans.sid.global.external.mail.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberInfoResponse memberInfo = memberService.getMemberInfo(memberId);

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

    @PostMapping("/{memberId}/update")
    public ResponseEntity<UpdateMemberResponse> updateMemberInfo(
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRequest updateMemberRequest) {

        UpdateMemberResponse updateMemberResponse = memberService.updateMemberInfo(memberId, updateMemberRequest);

        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }

    @PostMapping("/{memberId}/update/email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody UpdateEmailRequest updateEmailRequest, @PathVariable Long memberId) {
        emailService.sendEmailNotice(updateEmailRequest.getEmail(), memberId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

//    // FIXME: 인증 구현 후 경로 수정 필요
//    @PostMapping("/code/{memberId}/{code}")
//    public ResponseEntity<?> updateEmail(@PathVariable String code,) {
//        memberService.updateEmail(code);
//    }


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
