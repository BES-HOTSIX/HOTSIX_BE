package com.example.hotsix_be.payment.cashlog.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CashLog extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Long price;

    // toss 결제 시 발생 TODO 일반 결제에도 발생하도록 수정 예정
    // 하나의 예약에 관련한 (충전, 결제, 취소) 모든 cashLog가 한 orderId를 공유
    private String orderId;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonIgnore
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recharge_id")
    private Recharge recharge; // TODO relTypeCode 같은 구분법 만들기
}
