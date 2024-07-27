package org.devjeans.sid.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.devjeans.sid.global.exception.exceptionType.ExceptionType;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    String name; // exception의 이름
    int httpStatusCode; // http 상태코드
    String message; // 에러 메시지

    public static ErrorResponse of(ExceptionType e) {
        return ErrorResponse.builder()
                .name(e.name())
                .httpStatusCode(e.httpStatus().value())
                .message(e.message())
                .build();
    }
}
