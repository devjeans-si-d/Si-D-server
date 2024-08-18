package org.devjeans.sid.domain.chatRoom.dto.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SseChatResponse {
    private Long chatroomId;
    private String senderNickname;
    private String content;
}
