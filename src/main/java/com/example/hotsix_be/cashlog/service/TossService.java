package com.example.hotsix_be.cashlog.service;

import com.example.hotsix_be.cashlog.dto.request.TossConfirmRequest;
import com.example.hotsix_be.cashlog.dto.response.TossPaymentResponse;
import com.example.hotsix_be.cashlog.exception.CashException;
import lombok.Getter;
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
    private final CashLogService cashLogService;

    @Getter
    private static String tossPaymentsWidgetSecretKey;

    @Value("${custom.tossPayments.widget.secretKey}")
    public void setTossPaymentsWidgetSecretKey(String tossPaymentsWidgetSecretKey) {
        this.tossPaymentsWidgetSecretKey = tossPaymentsWidgetSecretKey;
    }

    @Transactional
    public Mono<TossPaymentResponse> confirmTossPayment(final TossConfirmRequest tossConfirmRequest) {

        String encodedKey = Base64.getEncoder().encodeToString((tossPaymentsWidgetSecretKey + ":").getBytes(UTF_8));
        String authorizations = "Basic " + encodedKey;

        return webClient.post()
                .uri("https://api.tosspayments.com/v1/payments/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizations)
                .bodyValue(tossConfirmRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> Mono.error(new CashException(PAYMENT_API_CALL_FAILED)))
                .bodyToMono(TossPaymentResponse.class);
    }
}
