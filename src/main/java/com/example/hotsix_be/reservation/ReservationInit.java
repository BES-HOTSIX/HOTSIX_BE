package com.example.hotsix_be.reservation;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;


@Component
@RequiredArgsConstructor
@Profile("dev")
@Transactional
@Order(2)
public class ReservationInit implements ApplicationRunner {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (reservationRepository.count() == 20) {
            List<Hotel> hotels = hotelRepository.findAll();
            Member member = memberRepository.findById(1L).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                Reservation reservation1 = new Reservation(
                        LocalDate.parse("2024-01-01"),
                        LocalDate.parse("2024-01-10"),
                        3L,
                        550000L,
                        true,
                        lastHotel,
                        member
                );
                reservationRepository.save(reservation1);

                Reservation reservation2 = new Reservation(
                        LocalDate.parse("2024-01-22"),
                        LocalDate.parse("2024-01-28"),
                        1L,
                        100000L,
                        true,
                        lastHotel,
                        member
                );
                reservationRepository.save(reservation2);

                Reservation reservation3 = new Reservation(
                        LocalDate.parse("2024-02-22"),
                        LocalDate.parse("2024-02-26"),
                        4L,
                        1000000L,
                        true,
                        lastHotel,
                        member
                );
                reservationRepository.save(reservation3);

                Reservation reservation4 = new Reservation(
                        LocalDate.parse("2024-02-14"),
                        LocalDate.parse("2024-02-15"),
                        2L,
                        1000000L,
                        true,
                        lastHotel,
                        member
                );
                reservationRepository.save(reservation4);

                Reservation reservation5 = new Reservation(
                        LocalDate.parse("2024-02-17"),
                        LocalDate.parse("2024-02-18"),
                        4L,
                        1000000L,
                        true,
                        lastHotel,
                        member
                );
                reservationRepository.save(reservation5);

                List<Reservation> reservations = reservationRepository.findAll();
                if (!reservations.isEmpty()) {
                    Reservation reservation = reservations.get(20);
                    reservation.cancelDone();
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}