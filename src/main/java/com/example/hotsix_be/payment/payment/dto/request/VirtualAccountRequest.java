package com.example.hotsix_be.payment.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "가상계좌번호")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@ToString
public class VirtualAccountRequest {

    @Schema(description = "가상계좌번호", example = "12312345612345")
    private String accountNumber;

    @Schema(description = "입금자명", example = "홍길동")
    private String customerName;
}
