package org.devjeans.sid.domain.chatRoom.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();
}
