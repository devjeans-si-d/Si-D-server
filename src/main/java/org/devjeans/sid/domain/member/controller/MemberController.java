package org.devjeans.sid.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.*;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.service.MemberService;
import org.devjeans.sid.global.external.mail.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.auth.entity.KakaoRedirect;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo() {
        MemberInfoResponse memberInfo = memberService.getMemberInfo();

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateMemberResponse> updateMemberInfo(@RequestBody UpdateMemberRequest updateMemberRequest) {

        UpdateMemberResponse updateMemberResponse = memberService.updateMemberInfo(updateMemberRequest);

        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }

    @PostMapping("/update/email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody UpdateEmailRequest updateEmailRequest) {
        emailService.sendEmailNotice(updateEmailRequest.getEmail());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/email-code/{code}")
    public ResponseEntity<UpdateEmailResponse> updateEmail(@PathVariable String code) {
        UpdateEmailResponse updateEmailResponse = memberService.updateMemberEmail(code);
        return new ResponseEntity<>(updateEmailResponse, HttpStatus.OK);
    }


}
