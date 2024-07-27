package org.devjeans.sid.global.exception.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public enum ChatExceptionType implements ExceptionType {
    INVALID_CHATROOM(BAD_REQUEST, "유효하지 않은 채팅방입니다."),
    NO_CHATROOM(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    NO_RECENT_MESSAGE(NOT_FOUND, "채팅방에 메시지가 없습니다."),
    CHATROOM_ALREADY_EXIST(BAD_REQUEST, "채팅방이 이미 존재합니다."),
    NOT_A_PARTICIPANT(BAD_REQUEST, "해당 채팅방에 속해있지 않은 유저입니다."),
    NO_RECEIVER(BAD_REQUEST, "상대방이 없습니다.");

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