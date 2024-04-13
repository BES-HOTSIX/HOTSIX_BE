package com.example.hotsix_be.common;

import com.example.hotsix_be.hotel.HotelInit;
import com.example.hotsix_be.member.MemberInit;
import com.example.hotsix_be.payment.cashlog.CashLogInit;
import com.example.hotsix_be.reservation.ReservationInit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// fly.io 배포 위해 @Profile 어노테이션 삭제
@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;

    private final MemberInit memberInit;

    private final HotelInit hotelInit;

    private final ReservationInit reservationInit;

    private final CashLogInit cashLogInit;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberInit.init();
        hotelInit.init();
        reservationInit.init();
        cashLogInit.init();
    }
}
