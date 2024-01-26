package com.example.hotsix_be.cashlog;

import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Order(3)
public class CashLogInit implements ApplicationRunner {
    private final CashLogRepository cashLogRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (hotelRepository.count() > 20) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                CashLog cashLog = CashLog.builder()
                        .eventType(EventType.충전__무통장입금)
                        .price(50_000L)
                        .member(null)
                        .reservation(null)
                        .withdrawApply(null)
                        .build();

                cashLogRepository.save(cashLog);
            });
        }
    }
}
