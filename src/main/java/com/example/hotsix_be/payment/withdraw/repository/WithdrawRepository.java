package com.example.hotsix_be.payment.withdraw.repository;

import com.example.hotsix_be.payment.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
}
