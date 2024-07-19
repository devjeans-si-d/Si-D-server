package org.devjeans.sid.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
public class ErrorResponse {
    String name; // exception의 이름
    HttpStatus httpStatus; // http 상태코드
    String message; // 에러 메시지
}
