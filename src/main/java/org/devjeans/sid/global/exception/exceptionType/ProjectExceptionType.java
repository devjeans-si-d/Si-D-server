package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum ProjectExceptionType implements ExceptionType {
    PROJECT_NOT_FOUND(BAD_REQUEST, "프로젝트가 존재하지 않습니다."),
    INVALID_IMAGE_FILE(BAD_REQUEST,"이미지 파일이 업로드 되지 않았습니다."),
    PROJECT_ALREADY_DELETED(BAD_REQUEST, "해당 프로젝트는 이미 삭제되었습니다."),
    PROJECT_ALREADY_CLOSED(BAD_REQUEST,"해당 프로젝트는 이미 마감되었습니다."),
    UNAUTHORIZED_ACTION(BAD_REQUEST,"삭제 권한이 없습니다.");

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