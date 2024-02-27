package com.example.hotsix_be.common;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse("2024-01-01");
        LocalDate endDate = LocalDate.parse("2024-01-10");

        if (memberRepository.count() <= 0 && hotelRepository.count() <= 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                Member member = new Member("test" + i, passwordEncoder.encode("test" + i), "test" + i, "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg", "test" + i);

                memberRepository.save(member);

                Hotel hotel = new Hotel("호텔", "서울" + i, "구로구" + i, 3L, 2L, 3L, 5L, List.of("주방"), "test" + i,
                        "test" + i, 10000L, member);

                hotel.addImage(new Image("test" + i, "test" + i,
                        "https://kr.object.ncloudstorage.com/hotsix.accomodation.bucket/ACCOMMODATION/2024/02/ACCOMMODATION_123_1afd0e02-780f-4732-8d62-a7453667d9de_pension1.jpg",
                        30L));
                hotelRepository.save(hotel);
            });

            IntStream.rangeClosed(1, 20).forEach(j -> {
                String sql =
                        "INSERT INTO reservations (check_in_date, check_out_date, guests, price, hotel_id, member_id, is_paid) "
                                +
                                "VALUES (?, ?, ?, ?, (SELECT id FROM hotels WHERE id = ?), (SELECT id FROM members ORDER BY id DESC LIMIT 1), ?)";

                jdbcTemplate.update(sql, startDate, endDate, 3 + j, 550000L, 1L, true);
            });
        }
    }
}
