package org.devjeans.sid.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.SearchMemberRequest;
import org.devjeans.sid.domain.member.service.MemberSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/member")
@RestController
//Todo : 추후 MemberController와 합치기
public class MemberSearchControlller {
    private final MemberSearchService memberSearchService;
    @Autowired
    public MemberSearchControlller(MemberSearchService memberSearchService){
        this.memberSearchService=memberSearchService;
    }
    @GetMapping("/list")
    public ResponseEntity<?> memberList(SearchMemberRequest searchMemberRequest, Pageable pageable){
        Page<MemberInfoResponse> memberLists = memberSearchService.memberList(searchMemberRequest,pageable);
        return new ResponseEntity<>(memberLists, HttpStatus.OK);
    }
}
