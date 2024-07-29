package org.devjeans.sid.global.external.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberIdEmailCode;
import org.devjeans.sid.domain.member.dto.UpdateEmailResponse;
import org.devjeans.sid.domain.member.repository.MemberRepository;
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
    private final RedisTemplate<String, MemberIdEmailCode> redisTemplate;

    @Async
    public void sendEmailNotice(String email, Long memberId){
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

    public String generateRandomCode(){
        return UUID.randomUUID().toString();
    }

    //thymeleaf를 통한 html 적용
    public String setContext(String randomCode) {
        Context context = new Context();
        context.setVariable("randomCode", randomCode);
        return templateEngine.process("emailVerification", context);
    }

    private void saveRamdomCode(String randomCode, Long memberId, String email) {
        MemberIdEmailCode dto = new MemberIdEmailCode(memberId, email);
        redisTemplate.opsForValue().set(randomCode, dto, Duration.ofMinutes(10));
    }


}