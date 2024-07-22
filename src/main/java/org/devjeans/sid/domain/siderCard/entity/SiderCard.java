package org.devjeans.sid.domain.siderCard.entity;

import org.devjeans.sid.domain.career.entity.Career;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.siderCardTechStack.entity.SiderCardTechStack;
import org.devjeans.sid.domain.socialInfo.entity.SocialInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SiderCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sider_card_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(length=10)
    private String domain;

    private String introduction;

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<SocialInfo> socialInfos = new ArrayList<>();

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<SiderCardTechStack> siderCardTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();
}
