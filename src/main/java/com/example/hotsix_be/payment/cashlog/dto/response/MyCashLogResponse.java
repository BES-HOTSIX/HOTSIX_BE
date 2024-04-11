package com.example.hotsix_be.payment.cashlog.dto.response;

import com.example.hotsix_be.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "결제 내역 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MyCashLogResponse {

    @Schema(description = "현재 로그인한 유저의 username", example = "KIM")
    private final String username;

    @Schema(description = "현재 로그인한 유저의 캐시 잔액", example = "1000000")
    private final Long restCash;

    @Schema(description = "현재 로그인한 유저의 이메일", example = "test1@gmail.com")
    private final String email;

    @Schema(description = "현재 로그인한 유저의 캐시 사용 내역 페이지")
    private final Page<CashLogConfirmResponse> cashLogConfirmPage;

    public static MyCashLogResponse of(
            final Member member,
            final Page<CashLogConfirmResponse> cashLogConfirmPage
    ) {
        return MyCashLogResponse.of(
                member.getUsername(),
                member.getRestCash(),
                member.getEmail(),
                cashLogConfirmPage
        );
    }

    public static MyCashLogResponse of(
            final String username,
            final Long restCash,
            final String email,
            final Page<CashLogConfirmResponse> cashLogConfirmPage
    ) {
        return new MyCashLogResponse(
                username,
                restCash,
                email,
                cashLogConfirmPage
        );
    }
}
