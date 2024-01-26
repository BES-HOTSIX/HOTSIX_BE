package com.example.hotsix_be.common;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Transactional
@Order(1)
public class DataInit implements ApplicationRunner {

    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (memberRepository.count() <= 0  && hotelRepository.count() <= 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Member member = new Member("test" + i, passwordEncoder.encode("test" + i), "test" + i);

                memberRepository.save(member);

                Hotel hotel = new Hotel("호텔", "서울" + i, "구로구" + i, 3L, 2L, 3L, 5L, List.of("주방"), "test" + i,
                        "test" + i, 10000L, member);

                hotel.addImage(new Image("test" + i, "test" + i, "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/suite/180708-55-2000-acc-seoul-signiel.jpg.thumb.1440.1440.jpg", 30L));
                hotelRepository.save(hotel);
            });
        }
    }
}
