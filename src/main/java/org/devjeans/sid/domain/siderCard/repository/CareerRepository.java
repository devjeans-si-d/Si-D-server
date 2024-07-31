package org.devjeans.sid.domain.siderCard.repository;

import org.devjeans.sid.domain.siderCard.entity.Career;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    Optional<Career> deleteCareerBySiderCard(SiderCard siderCard);
}
