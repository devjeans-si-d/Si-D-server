package org.devjeans.sid.global.external.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberIdEmailCode;
import org.devjeans.sid.domain.member.dto.UpdateEmailResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.UUID;
import java.util.UUID.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final RedisTemplate<String, MemberIdEmailCode> redisTemplate;
    private final SecurityUtil securityUtil;

    @Async
    public void sendEmailNotice(String email){
        Long memberId = securityUtil.getCurrentMemberId();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String randomCode = generateRandomCode();
            saveRamdomCode(randomCode, memberId, email); // redis에 저장 (ttl: 10분)

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("[Si-D] 이메일 변경을 위한 인증 메일입니다."); // 메일 제목
            mimeMessageHelper.setText(setContext(randomCode), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    // 합격 이메일
    @Async
    public void sendEmailNoticeForAccept(String email, Long memberId, Long projectId){
        Member member = memberRepository.findByIdOrThrow(memberId);
        Project project = projectRepository.findByIdOrThrow(projectId);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String randomCode = generateRandomCode();
            saveRamdomCode(randomCode, memberId, email); // redis에 저장 (ttl: 10분)

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("[Si-D] 프로젝트 참여 승인 안내 메일입니다."); // 메일 제목
            mimeMessageHelper.setText(setContext(project.getProjectName(), member.getName()), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    private String generateRandomCode(){
        return UUID.randomUUID().toString();
    }

    //thymeleaf를 통한 html 적용
    private String setContext(String randomCode) {
        Context context = new Context();
        context.setVariable("randomCode", randomCode);
        return templateEngine.process("emailVerification", context);
    }

    // 오버로딩
    private String setContext(String projectName, String memberName) {
        Context context = new Context();
        context.setVariable("projectName", projectName);
        context.setVariable("memberName", memberName);
        return templateEngine.process("acceptNotification", context);
    }

    private void saveRamdomCode(String randomCode, Long memberId, String email) {
        MemberIdEmailCode dto = new MemberIdEmailCode(memberId, email);
        redisTemplate.opsForValue().set(randomCode, dto, Duration.ofMinutes(10));
    }


}