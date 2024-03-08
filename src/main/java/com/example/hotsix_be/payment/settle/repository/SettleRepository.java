package com.example.hotsix_be.payment.settle.repository;

import com.example.hotsix_be.payment.settle.entity.Settle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettleRepository extends JpaRepository<Settle, Long> {
}
