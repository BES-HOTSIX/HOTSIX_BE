package com.example.hotsix_be.member.service;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @DisplayName("멤버의 비밀번호를 수정할 수 있다.")
    @Test
    void changePassword() {
        // given
        final Member member = new Member("홍길동", "oldPassword", "GILDONG", null, "test@test.com");

        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(memberRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(passwordEncoder.encode("newPassword")).willReturn("newPassword");

        // when
        memberService.changePassword(member.getId(), "newPassword");

        // then
        then(memberRepository).should().save(Mockito.argThat(
                updatedMember -> "newPassword".equals(updatedMember.getPassword())
        ));
    }

    @DisplayName("닉네임 중복을 검사할 수 있다.")
    @Test
    void isExistNickname() {
        // given
        String nickname = "GILDONG";
        given(memberRepository.existsByNickname(nickname)).willReturn(true);

        // when
        boolean exists = memberService.isExistNickname(nickname);

        // then
        assertThat(exists).isTrue();
    }
}
