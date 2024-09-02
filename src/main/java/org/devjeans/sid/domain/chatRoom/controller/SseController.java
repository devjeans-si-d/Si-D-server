package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.service.SseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sse")
@RestController
public class SseController {
    private final SseService sseService;
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        log.info("line 28: subscribe!!!!");
        return sseService.subscribe();
    }

    @GetMapping("/complete")
    public void completeEmitter() {
        sseService.completeEmitter();
    }


}
