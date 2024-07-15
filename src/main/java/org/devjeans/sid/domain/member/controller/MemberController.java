package org.devjeans.sid.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RestController
public class MemberController {
    // TODO: 삭제 예정
    @GetMapping
    public String testApi() {
        return "ok";
    }
}
