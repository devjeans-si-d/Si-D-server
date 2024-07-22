package org.devjeans.sid.domain.socialInfo.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;

import javax.persistence.*;

@Entity
public class SocialInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="social_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sider_card_id")
    private SiderCard siderCard;

    @Enumerated(EnumType.STRING)
    private Platform platform;
    private String url;


}
