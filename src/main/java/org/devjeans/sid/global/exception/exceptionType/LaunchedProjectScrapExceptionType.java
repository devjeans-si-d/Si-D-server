package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum LaunchedProjectScrapExceptionType implements ExceptionType{

        SCRAP_PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "스크랩 할 프로젝트가 존재하지 않습니다."),
        SCRAP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "스크랩 할 회원이 존재하지 않습니다."),
        SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "스크랩이 존재하지 않습니다.");

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
