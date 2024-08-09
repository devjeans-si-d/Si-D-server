package org.devjeans.sid.domain.siderCard.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.auth.dto.CommonResDto;
import org.devjeans.sid.domain.siderCard.dto.SiDCardListDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardUpdateReqDto;
import org.devjeans.sid.domain.siderCard.service.SiderCardService;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sider-card")
public class SiderCardController {

    private final SiderCardService siderCardService;
    private final SecurityUtil securityUtil;

    @GetMapping("")
    public ResponseEntity<?> siderCard(){
        Long id = securityUtil.getCurrentMemberId();
        SiderCardResDto siderCardResDto =  siderCardService.getSiderCard(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard is successfully retrieved",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateSiderCard(@RequestBody SiderCardUpdateReqDto dto){
        SiderCardResDto siderCardResDto = siderCardService.updateSiderCard(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard is successfully updated",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<?> siderCardList(Pageable pageable){
        Page<SiDCardListDto> siderCardResDtos =  siderCardService.getSiderCardList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard List is successfully retrieved",siderCardResDtos);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<?> siderCardById(@PathVariable("id") Long id){
        SiderCardResDto siderCardResDto =  siderCardService.getSiderCard(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"siderCard is successfully retrieved",siderCardResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


}
