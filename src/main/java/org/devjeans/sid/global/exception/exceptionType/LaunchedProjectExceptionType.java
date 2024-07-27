package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum LaunchedProjectExceptionType implements ExceptionType{

        PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않습니다."),
        INVALID_PROJECT_IMAGE(BAD_REQUEST, "잘못된 형식의 사진입니다."),
        INVALID_REQUEST(BAD_REQUEST, "잘못된 요청 데이터입니다.");

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
