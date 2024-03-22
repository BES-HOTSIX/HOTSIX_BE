package com.example.hotsix_be.reservation;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.payment.settle.entity.Settle;
import com.example.hotsix_be.payment.settle.service.SettleService;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.IntStream;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;


@Component
@RequiredArgsConstructor
public class ReservationInit {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;
    private final SettleService settleService;

    @Transactional
    public void init() {
        if (reservationRepository.count() < 5) {
            List<Hotel> hotels = hotelRepository.findAll();
            Member member = memberRepository.findById(10L).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate thisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .plusDays(i);

                    Reservation reservation = new Reservation(
                            thisWeek,
                            thisWeek.plusDays(3),
                            3L,
                            550000L,
                            true,
                            lastHotel,
                            member
                    );

                    reservation.updateOrderId("o" + randomNanoId());

                    reservationRepository.save(reservation);
                });

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate lastWeek = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
                            .minusDays(i);

                    Reservation reservation = new Reservation(
                            lastWeek,
                            lastWeek.plusDays(3),
                            3L,
                            550000L,
                            true,
                            lastHotel,
                            member
                    );

                    reservation.updateOrderId("o" + randomNanoId());

                    reservationRepository.save(reservation);
                });

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate weekBeforeLast = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
                            .minusDays(7 + i);

                    Reservation reservation = new Reservation(
                            weekBeforeLast,
                            weekBeforeLast.plusDays(3),
                            3L,
                            550000L,
                            true,
                            lastHotel,
                            member
                    );

                    Settle settle = settleService.doSettle(reservation);

                    reservation.updateOrderId("o" + randomNanoId());

                    reservation.updateOrderId("o" + randomNanoId());

                    settleService.save(settle);

                    reservationRepository.save(reservation);
                });

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate weekBeforeTheWeekBeforeLast = LocalDate.now()
                            .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusDays(14 + i);

                    Reservation reservation = new Reservation(
                            weekBeforeTheWeekBeforeLast,
                            weekBeforeTheWeekBeforeLast.plusDays(3),
                            3L,
                            550000L,
                            true,
                            lastHotel,
                            member
                    );

                    reservation.updateOrderId("o" + randomNanoId());

                    reservationRepository.save(reservation);
                });
            }
        }
    }
}