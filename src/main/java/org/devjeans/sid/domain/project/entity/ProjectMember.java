package org.devjeans.sid.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;

import javax.persistence.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProjectMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;

}
