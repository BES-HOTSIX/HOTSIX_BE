package com.example.hotsix_be.hotel;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Order(1)
public class HotelInit implements ApplicationRunner {

    private final HotelRepository hotelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (hotelRepository.count() <= 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Hotel hotel = new Hotel("호텔", "서울" + i, "구로구" + i, 3L, 2L, 3L, 5L, List.of("주차장"), "test" + i,
                        "test" + i, 10000L);

                hotelRepository.save(hotel);
            });
        }
    }
}
