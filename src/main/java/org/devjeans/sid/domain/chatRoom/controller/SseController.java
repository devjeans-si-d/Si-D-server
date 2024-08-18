package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.service.SseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/sse")
@RestController
public class SseController {
    private final SseService sseService;
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return sseService.subscribe();
    }

    @GetMapping("/complete")
    public void completeEmitter() {
        sseService.completeEmitter();
    }


}
