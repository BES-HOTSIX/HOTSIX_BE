package com.example.hotsix_be.payment.payment.service;

import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Profile("test")
public class TestTossServiceImplForEasyPay implements TossService {
    @Override
    public Mono<TossPaymentRequest> confirmTossPayment(TossConfirmRequest tossConfirmRequest) {
        TossPaymentRequest testRequest = new TossPaymentRequest(
                tossConfirmRequest.getOrderId(),
                Long.parseLong(tossConfirmRequest.getAmount()),
                "간편결제",
                "DONE",
                null,
                null,
                null
        );

        return Mono.just(testRequest);
    }
}
