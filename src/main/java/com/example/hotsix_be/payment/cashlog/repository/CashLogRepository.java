package com.example.hotsix_be.payment.cashlog.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
    Page<CashLog> findAllByMember(Member member, Pageable sortedPageable);

    Long countByMemberId(final Long id);
}
