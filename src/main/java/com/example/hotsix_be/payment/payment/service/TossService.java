package com.example.hotsix_be.payment.payment.service;


import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

import static com.example.hotsix_be.common.exception.ExceptionCode.PAYMENT_API_CALL_FAILED;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TossService {
    private final WebClient webClient;

    private static String authorization;

    @Value("${custom.tossPayments.widget.secretKey}")
    private void setTossPaymentsWidgetSecretKey(String tossPaymentsWidgetSecretKey) {
        String encodedKey = Base64.getEncoder().encodeToString((tossPaymentsWidgetSecretKey + ":").getBytes(UTF_8));
        authorization = "Basic " + encodedKey;
    }

    @Transactional
    public Mono<TossPaymentRequest> confirmTossPayment(final TossConfirmRequest tossConfirmRequest) {

        return webClient.post()
                .uri("https://api.tosspayments.com/v1/payments/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .bodyValue(tossConfirmRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> Mono.error(new PaymentException(PAYMENT_API_CALL_FAILED)))
                .bodyToMono(TossPaymentRequest.class);
    }
}
