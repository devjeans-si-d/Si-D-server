package org.devjeans.sid.domain.member.controller;

import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.MemberExceptionType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.*;

@RequestMapping("/api/member")
@RestController
public class MemberController {
    // TODO: 삭제 예정
    @GetMapping
    public String testApi() {
        throw new BaseException(MEMBER_NOT_FOUND);
    }

    // TODO: 삭제 예정
    @GetMapping("/exception")
    public String serverErrorTest() {
        throw new RuntimeException();
    }
}
