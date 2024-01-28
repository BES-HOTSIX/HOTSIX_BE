package com.example.hotsix_be.member.repository;

import com.example.hotsix_be.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialLoginId(String socialLoginId);

    boolean existsByNickname(String nicknameWithRandomNumber);

    void deleteMemberById(Long memberId);

    Optional<Member> findByUsername(String username);
}
