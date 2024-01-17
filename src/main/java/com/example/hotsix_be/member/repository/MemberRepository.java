package com.example.hotsix_be.member.repository;

import com.example.hotsix_be.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
