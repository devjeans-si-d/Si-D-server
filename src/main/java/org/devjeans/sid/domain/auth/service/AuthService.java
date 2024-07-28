package org.devjeans.sid.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.auth.entity.KakaoProfile;
import org.devjeans.sid.domain.auth.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.auth.entity.OAuthToken;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static java.rmi.server.LogStream.log;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;

    @Value("${auth.oauth.kakao.api}")
    private String authOauthKakaoApi;

    public Long login(KakaoRedirect kakaoRedirect) throws JsonProcessingException {
        OAuthToken oAuthToken = getAccessToken(kakaoRedirect);
        Long kakaoId = getKakaoId(oAuthToken.getAccess_token());
        return kakaoId;
    }

    public OAuthToken getAccessToken(KakaoRedirect kakaoRedirect) throws JsonProcessingException {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id",authOauthKakaoApi);
        params.add("redirect_uri","http://localhost:8080/api/auth/kakao/callback");
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

    public Member getMemberByKakaoId(Long kakaoId) {
        Member member = memberRepository.findBySocialId(kakaoId).orElse(null);
//        System.out.println(member);
        return member;
    }

    public Member delete() {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
//        System.out.println(id);
        Member member = memberRepository.findByIdOrThrow(id);
        member.updateDeleteAt();
        return member;
    }
}
