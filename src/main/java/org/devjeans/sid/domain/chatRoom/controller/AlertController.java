package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.dto.AlertResponse;
import org.devjeans.sid.domain.chatRoom.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AlertController {
    private final AlertService alertService;

    @GetMapping("/api/alert")
    public List<AlertResponse> getAllUnread() {
        return alertService.findAllUnread();
    }
}
