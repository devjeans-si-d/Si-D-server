package org.devjeans.sid.domain.siderCard.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.auth.dto.CommonResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.service.SiderCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sider-card")
public class SiderCardController {

    private final SiderCardService siderCardService;

    @GetMapping("")
    public ResponseEntity<?> siderCard(){
        SiderCardResDto siderCardResDto =  siderCardService.getSiderCard();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"order list is successfully retrieved",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }
}
