package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthException implements ExceptionType {
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "인가되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.");

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
