package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum ScrapProjectException implements ExceptionType{
    ALREADY_SCRAP_PROJECT(BAD_REQUEST, "이미 스크랩된 프로젝트입니다."),
    SCRAP_PROJECT_NOT_FOUND(BAD_REQUEST, "해당 프로젝트는 스크랩 되지 않았습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }

}
