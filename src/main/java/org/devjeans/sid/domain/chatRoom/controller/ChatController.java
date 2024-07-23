package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.chatService.ChatService;
import org.devjeans.sid.domain.chatRoom.dto.ChatRoomSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/member/{memberId}/list")
    public ResponseEntity<Page<ChatRoomSimpleResponse>> getChatRoomList(@PageableDefault(size = 5) Pageable pageable, @PathVariable("memberId") Long memberId) {
        Page<ChatRoomSimpleResponse> chatRoomList = chatService.getChatRoomList(pageable, memberId);

        return new ResponseEntity<>(chatRoomList, HttpStatus.OK);
    }
}
