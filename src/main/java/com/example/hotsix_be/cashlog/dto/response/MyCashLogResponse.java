package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MyCashLogResponse {
    private final Long restCash;
    private final PageImpl<CashLogConfirmResponse> cashLogConfirmPage;

    public static MyCashLogResponse of(
            final Member member,
            final PageImpl<CashLogConfirmResponse> cashLogConfirmPage
            ) {
        return new MyCashLogResponse(
                member.getRestCash(),
                cashLogConfirmPage
        );
    }
}
