package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectScrapRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.LAUNCHED_PROJECT_NOT_FOUND;

@Component
public class LaunchedProjectScheduler {

    private static final String LP_SCRAP_KEY_PREFIX = "launched_project";
    private static final String LP_VIEWS_KEY_PREFIX = "launched_project";

    private final LaunchedProjectRepository launchedProjectRepository;
    private final LaunchedProjectScrapRepository launchedProjectScrapRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String,String> viewRedisTemplate;
    private final RedisTemplate<String, Object> scrapRedisTemplate;

    @Autowired
    public LaunchedProjectScheduler(LaunchedProjectRepository launchedProjectRepository,
                                    LaunchedProjectScrapRepository launchedProjectScrapRepository,
                                    MemberRepository memberRepository,
                                    @Qualifier("LaunchedProjectView") RedisTemplate<String, String> viewRedisTemplate,
                                    @Qualifier("LaunchedProjectScrap") RedisTemplate<String, Object> scrapRedisTemplate
                                    ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.launchedProjectScrapRepository = launchedProjectScrapRepository;
        this.memberRepository = memberRepository;
        this.viewRedisTemplate = viewRedisTemplate;
        this.scrapRedisTemplate = scrapRedisTemplate;
    }

    @Qualifier("viewRedisTemplate")
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void syncViews(){
        for(LaunchedProject lp : launchedProjectRepository.findAll()){
            // 조회수 저장
            String key = LP_VIEWS_KEY_PREFIX + lp.getId() + "_views";
            String value = viewRedisTemplate.opsForValue().get(key);
            Long view = 0L;
            if(value!=null) view = Long.parseLong(value);
            lp.setViews(view);
            launchedProjectRepository.save(lp);
        }
    }

    @Qualifier("scrapRedisTemplate")
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void syncScraps(){
        for(LaunchedProject lp : launchedProjectRepository.findAll()){
            // scrap 저장
            // key : launched_project1_scrap (이런식으로 저장됨)
            String key = LP_SCRAP_KEY_PREFIX + lp.getId() + "_scrap";

            SetOperations<String, Object> setOperations = scrapRedisTemplate.opsForSet();
            Set<Object> members = setOperations.members(key); // scrap한 회원id Set

            if(members != null){
                for(Object memberId : members){
                    Long id = Long.parseLong(memberId.toString()); // 회원id

                    // RDB에 해당 스크랩 데이터가 이미 존재하는지 확인
                    boolean exists = launchedProjectScrapRepository.existsByLaunchedProjectIdAndMemberId(lp.getId(), id);
                    if(!exists){
                        // RDB에 저장
                        Member member = memberRepository.findByIdOrThrow(id);
                        LaunchedProjectScrap launchedProjectScrap = new LaunchedProjectScrap();
                        launchedProjectScrap.setLaunchedProject(lp);
                        launchedProjectScrap.setMember(member);
                        launchedProjectScrapRepository.save(launchedProjectScrap);
                    }
                }
            }
        }
    }



}
