package com.example.hotsix_be.reservation;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
@Profile("dev")
@Order(2)
public class ReservationInit implements ApplicationRunner {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (reservationRepository.count() == 0) {
            List<Hotel> hotels = hotelRepository.findAll();
            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                Reservation reservation1 = new Reservation(
                        LocalDate.parse("2024-01-01").atStartOfDay(),
                        LocalDate.parse("2024-01-10").atStartOfDay(),
                        3,
                        550000,
                        lastHotel,
                        true
                );
                reservationRepository.save(reservation1);

                Reservation reservation2 = new Reservation(
                        LocalDate.parse("2024-01-22").atStartOfDay(),
                        LocalDate.parse("2024-01-28").atStartOfDay(),
                        1,
                        100000,
                        lastHotel,
                        true
                );
                reservationRepository.save(reservation2);

                Reservation reservation3 = new Reservation(
                        LocalDate.parse("2024-02-22").atStartOfDay(),
                        LocalDate.parse("2024-02-28").atStartOfDay(),
                        4,
                        1000000,
                        lastHotel,
                        true
                );
                reservationRepository.save(reservation3);

                List<Reservation> reservations = reservationRepository.findAll();
                if (!reservations.isEmpty()) {
                    Reservation reservation = reservations.get(0);
                    reservation.updateCancelDate(LocalDateTime.now());
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
