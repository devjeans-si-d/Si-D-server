package org.devjeans.sid.domain.siderCard.repository;

import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.TechStackExceptionType.TECH_STACK_NOT_FOUND;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {

    default TechStack findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(TECH_STACK_NOT_FOUND));
    }
}
