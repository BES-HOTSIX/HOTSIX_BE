package com.example.hotsix_be.payment.refund.repository;

import com.example.hotsix_be.payment.refund.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
