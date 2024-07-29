package org.devjeans.sid.domain.siderCard.repository;

import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiderCardRepository extends JpaRepository<SiderCard, Long> {
}
