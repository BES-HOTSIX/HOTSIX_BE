package com.example.hotsix_be.payment.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@ToString
public class VirtualAccountRequest {
    private String accountNumber;

    private String customerName;
}
