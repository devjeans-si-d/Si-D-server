package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class LaunchedProjectScrapService {

    @Qualifier("LaunchedProjectScrap")
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SCRAP_KEY_PREFIX = "launched_project";
    // key : launched_project1_scrap (id=1일 때)

    public LaunchedProjectScrapService(@Qualifier("LaunchedProjectScrap") RedisTemplate<String, Object> redisTemplate,
                                       LaunchedProjectRepository launchedProjectRepository,
                                       MemberRepository memberRepository
    ){
        this.redisTemplate = redisTemplate;
    }

    public void addScrap(Long launchedProjectId, String memberId) {
        String key = SCRAP_KEY_PREFIX + launchedProjectId + "_scrap";
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        setOps.add(key, memberId);
    }

    public void removeScrap(Long launchedProjectId, String memberId) {
        String key = SCRAP_KEY_PREFIX + launchedProjectId + "_scrap";
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        setOps.remove(key, memberId);
    }

    public Long getScrapCount(Long launchedProjectId) {
        String key = SCRAP_KEY_PREFIX + launchedProjectId + "_scrap";
        return redisTemplate.opsForSet().size(key); // key의 Set에 있는 값의 개수 반환
    }

    public boolean isScraped(Long launchedProjectId, String memberId) {
        String key = SCRAP_KEY_PREFIX + launchedProjectId + "_scrap";
        return redisTemplate.opsForSet().isMember(key, memberId); //key의 Set에 memberId값이 포함되어있는지 확인
    }

}
