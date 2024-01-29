package com.example.hotsix_be.member.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.dto.response.MemberInfoResponse;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_USERNAME;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
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
}
