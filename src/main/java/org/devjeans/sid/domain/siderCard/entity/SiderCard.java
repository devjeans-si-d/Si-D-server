package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.career.entity.Career;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sider_card_id")
    private Long id;

//    트랜잭션으로 회원가입할때 사이더카드도 같이 생성하여 id 공유
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="member_id")
//    private Member member;

    @Column(length=10)
    private String domain;

    private String introduction;

//    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
//    private List<SocialInfo> socialInfos = new ArrayList<>();

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<SiderCardTechStack> siderCardTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();
}
