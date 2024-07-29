package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequiredArgsConstructor
public enum ProjectAcceptException implements ExceptionType {
    NOT_A_PM_MEMBER(FORBIDDEN, "해당 프로젝트 정보를 조회/수정할 권한이 없습니다."),
    CLOSED_PROJECT(BAD_REQUEST, "모집이 끝난 프로젝트입니다."),
    NO_APPLICATION_RECORD(BAD_REQUEST, "해당 회원이 지원한 내역이 없습니다."),
    DOUBLE_ACCEPT(BAD_REQUEST, "이미 승인 받은 회원입니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}