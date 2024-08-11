package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LaunchedProjectViewService {

    private final RedisTemplate<String,String> viewRedisTemplate;
    private final LaunchedProjectRepository launchedProjectRepository;
    private static final String LP_VIEWS_KEY_PREFIX = "launched_project";
    // key : launched_project1_view

    public LaunchedProjectViewService(@Qualifier("LaunchedProjectView")RedisTemplate<String, String> viewRedisTemplate,
                                      LaunchedProjectRepository launchedProjectRepository){
        this.viewRedisTemplate = viewRedisTemplate;
        this.launchedProjectRepository = launchedProjectRepository;
    }

    public void incrementViews(Long launchedProjectId) {
        launchedProjectRepository.findByIdOrThrow(launchedProjectId);
        String key = LP_VIEWS_KEY_PREFIX + launchedProjectId + "_views";
        viewRedisTemplate.opsForValue().increment(key);
    }

    public long getViews(Long launchedProjectId) {
        launchedProjectRepository.findByIdOrThrow(launchedProjectId);
        String key = LP_VIEWS_KEY_PREFIX + launchedProjectId + "_views";
        String views = viewRedisTemplate.opsForValue().get(key);
        return views != null ? Long.parseLong(views) : 0L; // views가 null이 아니면 views 문자열을 Long으로 형변환, null이면 0L
    }
}
