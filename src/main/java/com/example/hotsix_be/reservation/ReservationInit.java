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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
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
//@Profile("dev")
@Transactional
@Order(2)
public class ReservationInit implements ApplicationRunner {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;
    private final SettleService settleService;

    @Override
    public void run(ApplicationArguments args) {
        if (reservationRepository.count() < 5) {
            List<Hotel> hotels = hotelRepository.findAll();
            Member member = memberRepository.findById(10L).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate thisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusDays(i);

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
                    LocalDate lastWeek = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusDays(i);

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
                    LocalDate weekBeforeLast = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusDays(7 + i);

                    Reservation reservation = new Reservation(
                            weekBeforeLast,
                            weekBeforeLast.plusDays(3),
                            3L,
                            550000L,
                            true,
                            lastHotel,
                            member
                    );

                    reservation.updateOrderId("o" + randomNanoId());

                    Settle settle = settleService.doSettle(reservation);

                    settleService.save(settle);

                    reservationRepository.save(reservation);
                });

                IntStream.rangeClosed(0, 6).forEach(i -> {
                    LocalDate weekBeforeTheWeekBeforeLast = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusDays(14 + i);

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

//                Reservation reservation1 = new Reservation(
//                        LocalDate.parse("2024-01-01"),
//                        LocalDate.parse("2024-01-10"),
//                        3L,
//                        550000L,
//                        true,
//                        lastHotel,
//                        member
//                );
//                reservationRepository.save(reservation1);
//
//                Reservation reservation2 = new Reservation(
//                        LocalDate.parse("2024-01-22"),
//                        LocalDate.parse("2024-01-28"),
//                        1L,
//                        100000L,
//                        true,
//                        lastHotel,
//                        member
//                );
//                reservationRepository.save(reservation2);
//
//                Reservation reservation3 = new Reservation(
//                        LocalDate.parse("2024-02-22"),
//                        LocalDate.parse("2024-02-26"),
//                        4L,
//                        1000000L,
//                        true,
//                        lastHotel,
//                        member
//                );
//                reservationRepository.save(reservation3);
//
//                Reservation reservation4 = new Reservation(
//                        LocalDate.parse("2024-02-14"),
//                        LocalDate.parse("2024-02-15"),
//                        2L,
//                        1000000L,
//                        true,
//                        lastHotel,
//                        member
//                );
//                reservationRepository.save(reservation4);
//
//                Reservation reservation5 = new Reservation(
//                        LocalDate.parse("2024-02-17"),
//                        LocalDate.parse("2024-02-18"),
//                        4L,
//                        1000000L,
//                        true,
//                        lastHotel,
//                        member
//                );
//                reservationRepository.save(reservation5);

                List<Reservation> reservations = reservationRepository.findAll();
            }
        }
    }
}