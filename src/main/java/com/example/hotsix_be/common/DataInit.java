package com.example.hotsix_be.common;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Transactional
@Order(1)
public class DataInit implements ApplicationRunner {

    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (memberRepository.count() <= 0 && hotelRepository.count() <= 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Member member = new Member("test" + i, passwordEncoder.encode("test" + i), "test" + i);

                memberRepository.save(member);

                Hotel hotel = new Hotel("호텔", "서울" + i, "구로구" + i, 3L, 2L, 3L, 5L, List.of("주방"), "test" + i,
                        "test" + i, 10000L, member);

                hotel.addImage(new Image("test" + i, "test" + i, "https://kr.object.ncloudstorage.com/hotsix.accomodation.bucket/ACCOMODATION/2024/01/ACCOMODATION_123123_0926ac58-3dd4-4b2c-a044-e123f2d20e6b_%ED%8E%9C%EC%85%982.jpg", 30L));
                hotelRepository.save(hotel);
            });

            IntStream.rangeClosed(1, 20).forEach(j -> {
                Reservation reservation = new Reservation("2024-01-01", "2024-01-10", 3, 550000L, hotelRepository.findById(1L).get(), memberRepository.findAll().getLast());
                reservationRepository.save(reservation);
            });
        }
    }
}
