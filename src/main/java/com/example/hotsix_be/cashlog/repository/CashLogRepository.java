package com.example.hotsix_be.cashlog.repository;

import com.example.hotsix_be.cashlog.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
