package com.example.hotsix_be.reservation;

import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import com.example.hotsix_be.hotel.entity.Hotel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Order(2)
public class ReservationInit implements ApplicationRunner {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (hotelRepository.count() > 50) {
            List<Hotel> hotels = hotelRepository.findAll();
            if (!hotels.isEmpty()) {
                Hotel lastHotel = hotels.get(hotels.size() - 1);

                Reservation reservation = new Reservation(
                        "2024-01-22",
                        "2024-01-28",
                        3,
                        550000,
                        lastHotel
                );

                reservationRepository.save(reservation);
            }
        }
    }
}
