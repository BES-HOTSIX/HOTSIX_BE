package com.example.hotsix_be.payment.cashlog.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.QCashLog;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CashLogRepositoryImpl implements CashLogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CashLogConfirmResponse> getCashLogConfirmResByMember(Member member, Pageable pageable) {
        QCashLog cashLog = QCashLog.cashLog;

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder<?> orderByExpression = new PathBuilder<>(CashLog.class, "cashLog");
            orders.add(new OrderSpecifier<>(direction, orderByExpression.get(prop, String.class)));
        });

        OrderSpecifier<?>[] order = orders.toArray(OrderSpecifier[]::new);

        List<Tuple> content = jpaQueryFactory
                .select(
                        cashLog.id,
                        cashLog.eventType,
                        cashLog.amount,
                        cashLog.member.id,
                        cashLog.orderId,
                        cashLog.createdAt
                )
                .from(cashLog)
                .where(cashLog.member.eq(member))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(order)
                .fetch();

        List<CashLogConfirmResponse> res = content.stream()
                .map(tuple -> CashLogConfirmResponse.of(
                        tuple.get(cashLog.id),
                        tuple.get(cashLog.eventType),
                        tuple.get(cashLog.amount),
                        tuple.get(cashLog.member.id),
                        tuple.get(cashLog.orderId),
                        tuple.get(cashLog.createdAt)
                )).toList();

        Long totalCount = jpaQueryFactory.select(Wildcard.count)
                .from(cashLog)
                .where(cashLog.member.eq(member))
                .fetch().getFirst();

        return new PageImpl<>(res, pageable, totalCount);
    }
}
