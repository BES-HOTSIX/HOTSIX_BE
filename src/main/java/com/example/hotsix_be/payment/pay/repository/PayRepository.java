package com.example.hotsix_be.payment.pay.repository;

import com.example.hotsix_be.payment.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
