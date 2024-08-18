package org.devjeans.sid.domain.chatRoom.entity;

import lombok.*;
import org.devjeans.sid.domain.common.BaseEntity;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Alert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String isRead; // Y N
    private AlertType alertType;

    public void updateIsRead(String yn) {
        this.isRead = yn;
    }
}
