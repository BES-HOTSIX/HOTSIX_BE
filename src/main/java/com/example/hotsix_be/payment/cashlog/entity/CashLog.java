package com.example.hotsix_be.payment.cashlog.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn
@Entity
public class CashLog extends DateEntity {

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Long amount;

    private String orderId;

    // 결제 상태
    private LocalDateTime payDate;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;  // 이 거래의 주체

    public void updateCashLog(CashLog cashLog) {
        this.eventType = cashLog.getEventType();
        this.amount = cashLog.getAmount();
        this.orderId = cashLog.getOrderId();
        this.member = cashLog.getMember();
    }

    public boolean isInitialized() {
        return this.amount != null;
    }

    public boolean isPaid() {
        return this.payDate != null;
    }

    public void payDone() {
        this.payDate = LocalDateTime.now();
    }
}
