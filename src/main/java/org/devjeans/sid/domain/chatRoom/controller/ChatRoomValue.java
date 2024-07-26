package org.devjeans.sid.domain.chatRoom.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChatRoomValue {
    private Long memberId;
    private Long chatRoomId;
}
