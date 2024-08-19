package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.siderCard.dto.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//    트랜잭션으로 회원가입할때 사이더카드도 같이 생성하여 id 공유
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="member_id")
//    private Member member;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCard extends BaseEntity {
    @Id
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

    public SiderCardResDto fromEntity(Member member, List<LaunchedProjectResDto> dto) {
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
                .socialLinkRes(new SocialLinkDto(member.getEmail(),this.github,this.behance,this.linkedin,this.etc))
                .careerRes(careerResDtos)
                .teckStackRes(teckStackResDtos)
                .launchedProjectRes(dto)
                .build();
    }

    public SiderCard update(SiderCardUpdateReqDto dto,SiderCard siderCard,List<SiderCardTechStack> techStacks, Member member) {
        List<Career> careers = new ArrayList<>();
        for (CareerReqDto career : dto.getCareers()) {
            careers.add(career.toEntity(siderCard));
        }
        member.updateProfileImageUrl(dto.getImage());
        this.jobField = dto.getJobField();
        this.introduction = dto.getIntroduction();
        this.image = dto.getImage();
        this.email = dto.getSocialLink().getEmail();
        this.github = dto.getSocialLink().getGithub();
        this.behance = dto.getSocialLink().getBehance();
        this.linkedin = dto.getSocialLink().getLinkedin();
        this.etc = dto.getSocialLink().getEtc();
        this.careers = careers;
        this.siderCardTechStacks =techStacks;
        return this;
    }

    public SiDCardListDto listFromEntity(Member member) {
        return SiDCardListDto.builder()
                .member_id(this.id)
                .member_nickname(member.getNickname())
                .member_jobField(this.jobField)
                .member_image(this.image).build();
    }
}
