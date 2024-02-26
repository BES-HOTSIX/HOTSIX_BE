package com.example.hotsix_be.payment.cashlog.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CashLogRepositoryCustom {
    Page<CashLogConfirmResponse> getCashLogConfirmResByMember(Member member, Pageable pageable);
}