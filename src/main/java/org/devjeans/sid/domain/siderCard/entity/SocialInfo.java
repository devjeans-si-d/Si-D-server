package org.devjeans.sid.domain.siderCard.entity;

import org.devjeans.sid.domain.common.BaseEntity;

import javax.persistence.*;

@Entity
public class SocialInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="social_info_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sider_card_id")
    private SiderCard siderCard;

}
