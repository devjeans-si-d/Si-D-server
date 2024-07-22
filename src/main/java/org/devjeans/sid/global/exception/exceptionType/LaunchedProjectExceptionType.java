package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LaunchedProjectExceptionType implements ExceptionType{

        PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않습니다."),
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request data"),
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

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
