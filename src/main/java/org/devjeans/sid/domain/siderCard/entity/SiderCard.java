package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.siderCard.dto.CareerResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.dto.SocialLinkResDto;
import org.devjeans.sid.domain.siderCard.dto.TeckStackResDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//    트랜잭션으로 회원가입할때 사이더카드도 같이 생성하여 id 공유
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="member_id")
//    private Member member;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCard extends BaseEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sider_card_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobField jobField;
    private String introduction;
    private String image;

    private String email;
    private String github;
    private String behance;
    private String linkedin;
    private String etc;

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "siderCard", cascade = CascadeType.ALL)
    private List<SiderCardTechStack> siderCardTechStacks = new ArrayList<>();

    public SiderCardResDto fromEntity(Member member) {
        List<CareerResDto> careerResDtos = new ArrayList<>();
        for (Career career : this.careers) {
            careerResDtos.add(career.fromEntity());
        }
        List<TeckStackResDto> teckStackResDtos = new ArrayList<>();
        for(SiderCardTechStack teckStack : this.siderCardTechStacks) {
            teckStackResDtos.add(teckStack.fromEntity());
        }
        return SiderCardResDto.builder()
                .id(this.id)
                .nickname(member.getNickname())
                .jobField(this.jobField)
                .introduction(this.introduction)
                .image(this.image)
                .socialLinkRes(new SocialLinkResDto(this.email,this.github,this.behance,this.linkedin,this.etc))
                .careerRes(careerResDtos)
                .teckStackRes(teckStackResDtos)
                .build();
    }
}
