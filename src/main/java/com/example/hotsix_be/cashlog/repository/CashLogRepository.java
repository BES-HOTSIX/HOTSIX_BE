package com.example.hotsix_be.cashlog.repository;

import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
    Page<CashLog> findAllByMember(Member member, Pageable sortedPageable);
}
