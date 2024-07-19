package org.devjeans.sid.global.exception;

import lombok.Getter;
import org.devjeans.sid.global.exception.exceptionType.ExceptionType;

@Getter
public class BaseException extends RuntimeException {
    ExceptionType exceptionType;

    @Override
    public String getMessage() {
        return exceptionType.message();
    }
}
