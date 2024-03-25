package com.example.hotsix_be.hotel;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
public class HotelInit {
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void init() {

        if (memberRepository.count() <= 50 && hotelRepository.count() <= 0) {

            LongStream.rangeClosed(1, 50).forEach(i -> {
                Member member = memberRepository.findById(i).orElseThrow();

                Hotel hotel = new Hotel("호텔", "서울" + i, "구로구" + i, 3L, 2L, 3L, 5L, List.of("주방"), "test" + i,
                        "test" + i, 10000L, member);

                hotel.addImage(new Image("test" + i, "test" + i,
                        "https://kr.object.ncloudstorage.com/hotsix.accomodation.bucket/ACCOMMODATION/2024/02/ACCOMMODATION_123_1afd0e02-780f-4732-8d62-a7453667d9de_pension1.jpg",
                        30L));
                hotelRepository.save(hotel);
            });
        }
    }
}
