package org.devjeans.sid.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<UpdateMemberResponse> getMemberInfo(@PathVariable("memberId") Long memberId,
                                          @RequestPart String name,
                                          @RequestPart String nickname,
                                          @RequestPart String phoneNumber,
                                          @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
//        log.info("line 32 {}", updateMemberRequest);
        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
                .nickname(nickname)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
        UpdateMemberResponse updateMemberResponse = memberService.updateMemberInfo(memberId, updateMemberRequest, profileImage);

        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }
}
