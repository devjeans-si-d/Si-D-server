package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.service.ChatService;
import org.devjeans.sid.domain.chatRoom.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    // createdAt의 내림차순으로 뿌려주기, 보낸사람 아이디, 내용, 보낸 시간
    @GetMapping("/chat-room/{chatRoomId}/receiver/{memberId}")
    public ResponseEntity<Slice<ChatRoomMessageResponse>> getChatRoomMessages(@PageableDefault(size = 50) Pageable pageable,
                                                                              @PathVariable("chatRoomId") Long chatRoomId,
                                                                              @PathVariable("memberId") Long memberId) {
        Slice<ChatRoomMessageResponse> chatRoomMessages = chatService.getChatRoomMessages(pageable, chatRoomId, memberId);

        return new ResponseEntity<>(chatRoomMessages, HttpStatus.OK);
    }

    // 채팅방 만들기
    @PostMapping("/chatroom/create")
    public ResponseEntity<CreateChatRoomResponse> createChatRoom(@RequestBody CreateChatRoomRequest createChatRoomRequest) {
        CreateChatRoomResponse chatRoom = chatService.createChatRoom(createChatRoomRequest);

        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
    }

    // 채팅방 입장하기 (= 메모리 map에 값 넣기)
    // 웹 소켓 연결은 클라이언트에서 함
    @PostMapping("/chatroom/{chatroomId}/entrance/member/{memberId}")
    public void enterChatRoom(@PathVariable Long chatroomId, @PathVariable Long memberId) {
        chatService.enterChatRoom(chatroomId, memberId);
    }

}
