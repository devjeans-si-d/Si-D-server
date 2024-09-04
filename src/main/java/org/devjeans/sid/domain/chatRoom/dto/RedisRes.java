package org.devjeans.sid.domain.chatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisRes {
    private String type;
    private Object data;
}
