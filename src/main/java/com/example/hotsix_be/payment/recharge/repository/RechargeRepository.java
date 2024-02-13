package com.example.hotsix_be.payment.recharge.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    Optional<Recharge> findByOrderId(String orderId);

    Page<Recharge> findAllByRecipient(Member recipient, Pageable sortedPageable);

    Optional<Recharge> findByOrderIdContainingAndRecipient(String orderId, Member member);
}
