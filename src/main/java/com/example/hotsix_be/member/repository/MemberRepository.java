package com.example.hotsix_be.member.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.SocialProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByNicknameAndAndSocialProvider(String nickname, SocialProvider socialProvider);

    Optional<Member> findBySocialLoginId(String socialLoginId);

    boolean existsByNickname(String nicknameWithRandomNumber);

    void deleteMemberById(Long memberId);

    Optional<Member> findByUsername(String username);
}
