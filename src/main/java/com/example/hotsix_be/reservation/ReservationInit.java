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

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> 38513d6c132fb2ae4e64dff1e2e9aaa6d7c201bc
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
<<<<<<< HEAD
        if (reservationRepository.count() < 30) {
=======
        if (reservationRepository.count() == 0) {
>>>>>>> 38513d6c132fb2ae4e64dff1e2e9aaa6d7c201bc
            List<Hotel> hotels = hotelRepository.findAll();
            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                Reservation reservation1 = new Reservation(
                        "2024-01-01",
                        "2024-01-10",
                        3,
                        550000,
                        lastHotel
                );
                reservationRepository.save(reservation1);

                Reservation reservation2 = new Reservation(
                        "2024-01-22",
                        "2024-01-28",
                        1,
                        100000,
                        lastHotel
                );
                reservationRepository.save(reservation2);

                Reservation reservation3 = new Reservation(
                        "2024-02-22",
                        "2024-02-28",
                        4,
                        1000000,
                        lastHotel
                );
                reservationRepository.save(reservation3);

                List<Reservation> reservations = reservationRepository.findAll();
                if (!reservations.isEmpty()) {
                    Reservation reservation = reservations.get(0);
                    reservation.setCancelDate(LocalDateTime.now());
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
