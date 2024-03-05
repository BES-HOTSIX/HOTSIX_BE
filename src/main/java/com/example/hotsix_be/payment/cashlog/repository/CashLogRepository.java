package com.example.hotsix_be.payment.cashlog.repository;

import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long>, CashLogRepositoryCustom {
//    Page<CashLog> findAllByMember(Member member, Pageable sortedPageable);

    Long countByMemberId(final Long id);
}
