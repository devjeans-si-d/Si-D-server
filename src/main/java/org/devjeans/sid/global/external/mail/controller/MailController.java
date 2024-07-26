package org.devjeans.sid.global.external.mail.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.global.external.mail.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailController {
    private final EmailService emailService;
    @GetMapping("/api/mail/send/{emailAddress}")
    public String sendEmail(@PathVariable String emailAddress) {
        emailService.sendEmailNotice(emailAddress);

        return "ok";
    }
}
