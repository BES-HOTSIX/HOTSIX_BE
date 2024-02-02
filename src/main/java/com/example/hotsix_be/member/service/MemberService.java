package com.example.hotsix_be.member.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.service.ImageService;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.dto.response.MemberInfoResponse;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_USERNAME;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    // CashLog 에서 캐시 사용 내역 확인을 위해 만든 findById
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_BY_ID));
    }

    public void save(MemberRegisterRequest memberRegisterRequest) {

        final Member member = new Member(
                memberRegisterRequest.getUsername(),
                passwordEncoder.encode(memberRegisterRequest.getPassword()),
                memberRegisterRequest.getNickname()
        );

        memberRepository.save(member);
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_USERNAME));
    }

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        return MemberInfoResponse.of(member);
    }

    public void changePassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        member.changePassword(passwordEncoder.encode(password));

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isExistNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void changeNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        member.changeNickname(nickname);

        memberRepository.save(member);
    }

    public void changeImageUrl(Long memberId, final List<MultipartFile> multipartFiles) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        List<Image> newImages = imageService.uploadImages(multipartFiles, "ACCOMODATION",
                member.getNickname());

        member.changeImageUrl(newImages.getFirst().getUrl());

        memberRepository.save(member);
    }
}
