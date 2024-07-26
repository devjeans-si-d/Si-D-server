package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum MemberExceptionType implements ExceptionType {
    MEMBER_NOT_FOUND(BAD_REQUEST, "회원이 존재하지 않습니다."),
    INVALID_VERIFICATION(BAD_REQUEST, "잘못된 이메일 인증 요청입니다."),
    INVALID_PROFILE_IMAGE(BAD_REQUEST, "잘못된 형식의 프로필 사진입니다.");

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
