package org.devjeans.sid.domain.siderCard.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.auth.dto.CommonResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardUpdateReqDto;
import org.devjeans.sid.domain.siderCard.service.SiderCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sider-card")
public class SiderCardController {

    private final SiderCardService siderCardService;

    @GetMapping("")
    public ResponseEntity<?> siderCard(){
        SiderCardResDto siderCardResDto =  siderCardService.getSiderCard();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard is successfully retrieved",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateSiderCard(@RequestBody SiderCardUpdateReqDto dto){
        SiderCardResDto siderCardResDto = siderCardService.updateSiderCard(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard is successfully updated",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


}
