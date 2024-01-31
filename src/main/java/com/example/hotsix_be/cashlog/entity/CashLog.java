package com.example.hotsix_be.cashlog.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.withdrawapply.entity.WithdrawApply;
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
    @JoinColumn(name = "withdrawApply_id")
    private WithdrawApply withdrawApply;
}
