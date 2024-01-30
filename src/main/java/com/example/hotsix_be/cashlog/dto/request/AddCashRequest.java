package com.example.hotsix_be.cashlog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCashRequest {
    @NotBlank(message = "받는 사람의 아이디를 입력해주세요.") // TODO 무통장입금 구현하기
    private String username;
    @NotNull(message = "충전하실 금액을 입력해주세요.")
    private Long price;
}
