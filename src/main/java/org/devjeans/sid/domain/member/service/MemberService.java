package org.devjeans.sid.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.member.entity.KakaoProfile;
import org.devjeans.sid.domain.member.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.entity.OAuthToken;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityUtil securityUtil;

    @Value("${auth.oauth.kakao.api}")
    private String authOauthKakaoApi;
    public MemberInfoResponse getMemberInfo(Long memberId) {
        // TODO: 인증, 인가 구현 후 멤버 아이디와 시큐리티 컨텍스트의 멤버가 동일한지 확인하는 로직 필요

        Member member = memberRepository.findByIdOrThrow(memberId);
        return MemberInfoResponse.fromEntity(member);
    }

    @Transactional
    public UpdateMemberResponse updateMemberInfo(Long memberId,
                                                 UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        // 회원 정보 수정
        member.updateMemberInfo(updateMemberRequest);
        Member updatedMember = memberRepository.save(member);

        return UpdateMemberResponse.fromEntity(updatedMember);
    }

    @Transactional
    public void updateEmail(String email, String code) {
        Long memberId = securityUtil.
        Member member = memberRepository.findByIdOrThrow(memberId);
        if(getCodeFromRedis(memberId).equals(code)) {
            member.updateEmail(email); // 인증 성공
        } else {
            throw new BaseException(INVALID_VERIFICATION);
        }
    }

    private String getCodeFromRedis(Long memberId) {
        return (String) redisTemplate.opsForValue().get(memberId.toString());
    }


    public Long login(KakaoRedirect kakaoRedirect) throws JsonProcessingException {
        OAuthToken oAuthToken = getAccessToken(kakaoRedirect);
        Long kakaoId = getKakaoId(oAuthToken.getAccess_token());
        return kakaoId;
    }
    public void createMember(Long kakaoId){

    }

    public Member getMemberByKakaoId(Long kakaoId) {
        Member member = memberRepository.findBySocialId(kakaoId).orElse(null);
//        System.out.println(member);
        return member;
    }

    public OAuthToken getAccessToken(KakaoRedirect kakaoRedirect) throws JsonProcessingException {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id",authOauthKakaoApi);
        params.add("redirect_uri","http://localhost:8080/api/member/auth/kakao/callback");
        params.add("code",kakaoRedirect.getCode());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        return oAuthToken;
    }

    public Long getKakaoId(String accessToken) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();

//        HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

//        HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers);

//        Http 요청하기 = Post방식으로 그리고 response 변수의 응답 받음
        ResponseEntity<String> response2 = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);

        return kakaoProfile.getId();
    }

    public void registerMember(RegisterMemberRequest dto) {
        Member member = dto.toEntity();
        memberRepository.save(member);
    }
}
