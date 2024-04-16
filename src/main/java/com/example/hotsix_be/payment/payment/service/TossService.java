package com.example.hotsix_be.payment.payment.service;

import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import reactor.core.publisher.Mono;

public interface TossService {
    Mono<TossPaymentRequest> confirmTossPayment(final TossConfirmRequest tossConfirmRequest);
}
