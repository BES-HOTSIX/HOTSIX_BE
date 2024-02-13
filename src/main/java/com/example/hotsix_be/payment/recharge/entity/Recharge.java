package com.example.hotsix_be.payment.recharge.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Recharge extends DateEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long amount;

    private String orderId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    // 결제 상태
    private boolean isPaid;

    // 입금자명
    private String depositor;

    // 가상계좌
    private String accountNumber;

    // 웹훅과 비교할 때 사용할 시크릿
    private String secret;

    private LocalDateTime cancelDate;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member recipient;

    public void payDone() {
        this.isPaid = true;
    }

    public void cancelDone() {
        cancelDate = LocalDateTime.now();
    }

    public boolean isCancelled() {
        return cancelDate != null;
    }
}
