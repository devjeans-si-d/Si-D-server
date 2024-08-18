package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.Alert;
import org.devjeans.sid.domain.chatRoom.entity.AlertType;
import org.devjeans.sid.domain.common.BaseEntity;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlertResponse extends BaseEntity {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String isRead; // Y N
    private AlertType alertType;

    public static AlertResponse fromEntity(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .title(alert.getTitle())
                .content(alert.getContent())
                .isRead(alert.getIsRead())
                .alertType(alert.getAlertType())
                .memberId(alert.getMemberId())
                .build();
    }
}
