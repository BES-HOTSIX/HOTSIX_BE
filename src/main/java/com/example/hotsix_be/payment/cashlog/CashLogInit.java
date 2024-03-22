package com.example.hotsix_be.payment.cashlog;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.repository.CashLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.충전__무통장입금;

@Component
@RequiredArgsConstructor
public class CashLogInit {
    private final CashLogRepository cashLogRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void init() {
        if (cashLogRepository.countByMemberId(1L) < 20) {
            Member member = memberRepository.findById(1l).orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_BY_ID));

            IntStream.rangeClosed(1, 50).forEach(i -> {
                CashLog cashLog = CashLog.builder()
                        .eventType(충전__무통장입금)
                        .amount(50_000L)
                        .member(member)
                        .orderId(randomNanoId())
                        .build();

                cashLogRepository.save(cashLog);
            });
        }
    }
}
