package com.example.hotsix_be.cashlog;

import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Order(3)
public class CashLogInit implements ApplicationRunner {
    private final CashLogRepository cashLogRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (cashLogRepository.countByMemberId(1L) < 20) {
            Member member = memberRepository.findById(1l).orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_BY_ID));

            IntStream.rangeClosed(1, 50).forEach(i -> {
                CashLog cashLog = CashLog.builder()
                        .eventType(EventType.충전__무통장입금)
                        .price(50_000L)
                        .member(member)
                        .reservation(null)
                        .withdrawApply(null)
                        .build();

                cashLogRepository.save(cashLog);
            });
        }
    }
}
