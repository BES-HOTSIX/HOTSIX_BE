package com.example.hotsix_be.member.service;

import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public void save(MemberRegisterRequest memberRegisterRequest) {

        final Member member = new Member(
                memberRegisterRequest.getUsername(),
                memberRegisterRequest.getPassword(),
                memberRegisterRequest.getNickname()
        );

        memberRepository.save(member);
    }

}
