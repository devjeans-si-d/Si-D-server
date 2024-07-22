package org.devjeans.sid.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.INVALID_PROFILE_IMAGE;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private static final String STORAGE_DIR = "/Users/sejeong/Documents/besw-devjeans/temp"; // 실제 저장 경로로 변경
    public MemberInfoResponse getMemberInfo(Long memberId) {
        // TODO: 인증, 인가 구현 후 멤버 아이디와 시큐리티 컨텍스트의 멤버가 동일한지 확인하는 로직 필요

        Member member = memberRepository.findByIdOrThrow(memberId);
        return MemberInfoResponse.fromEntity(member);
    }

    @Transactional
    public UpdateMemberResponse updateMemberInfo(Long memberId,
                                                 UpdateMemberRequest updateMemberRequest,
                                                 MultipartFile profileImage) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        // 회원의 프로필 사진을 스토리지에 저장
        if (profileImage != null && !profileImage.isEmpty()) {
            String newFileName = memberId + "_" + profileImage.getOriginalFilename();
            Path path = Paths.get(STORAGE_DIR, newFileName);

            try {
                Files.write(path, profileImage.getBytes());
            } catch(IOException e) {
                throw new BaseException(INVALID_PROFILE_IMAGE);
            }


            member.updateProfileImageUrl(path.toString());

        }

        // 회원 정보 수정
        member.updateMemberInfo(updateMemberRequest);
        Member updatedMember = memberRepository.save(member);

        return UpdateMemberResponse.fromEntity(updatedMember);
    }
}
