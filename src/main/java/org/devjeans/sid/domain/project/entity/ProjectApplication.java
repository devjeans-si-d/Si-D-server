package org.devjeans.sid.domain.project.entity;

import lombok.*;
import org.devjeans.sid.domain.common.BaseEntity;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_application_id")
    private Long id;

    private Long memberId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private JobField jobField;

    private Boolean isAccepted;

    private String content; // 지원 내용

    public void updateIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}