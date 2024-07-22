package org.devjeans.sid.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.devjeans.sid.global.exception.exceptionType.ExceptionType;

@RequiredArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    private final ExceptionType exceptionType;

    @Override
    public String getMessage() {
        return exceptionType.message();
    }
}
