package com.example.hotsix_be.payment.refund.service;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.refund.repository.RefundRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RefundServiceTest {
    @InjectMocks
    private RefundService refundService;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private CashLogService cashLogService;

    private Member guest;
    private Reservation reservation;

    @BeforeEach
    void initData() {
        guest = new Member(
                1L,
                "KIM",
                "KIM",
                null
        );
        guest.assignRole(Role.GUEST);

        Member host = new Member(
                2L,
                "techit",
                "techit",
                null
        );
        host.assignRole(Role.HOST);
        Hotel hotel = new Hotel(
                "호텔",
                "서울시 강남구",
                "강남역 1번출구",
                10L,
                10L,
                10L,
                10L,
                List.of("헬스장"),
                "강남호텔",
                "강남호텔입니다.",
                10000L,
                host
        );
        reservation = new Reservation(
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(6),
                2L,
                100000L,
                false,
                hotel,
                guest
        );
    }

    @Test
    @DisplayName("예약 취소를 진행한다.")
    void doRefund() {
        // given

        // when
        refundService.doRefund(reservation, 0L);

        // then
    }
}
