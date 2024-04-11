package com.example.hotsix_be.payment.cashlog.entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public abstract class CashLogMarker extends CashLog {
}
