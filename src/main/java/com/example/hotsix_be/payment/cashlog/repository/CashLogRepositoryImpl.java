package com.example.hotsix_be.payment.cashlog.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.hotsix_be.payment.cashlog.entity.QCashLog.cashLog;

@Repository
@RequiredArgsConstructor
public class CashLogRepositoryImpl implements CashLogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CashLogConfirmResponse> getCashLogConfirmResForPayByMember(Member member, Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder<?> orderByExpression = new PathBuilder<>(CashLog.class, "cashLog");
            orders.add(new OrderSpecifier<>(direction, orderByExpression.get(prop, String.class)));
        });

        OrderSpecifier<?>[] order = orders.toArray(OrderSpecifier[]::new);

        BooleanExpression condition1 = cashLog.member.eq(member);
        BooleanExpression condition2 = cashLog.dtype.eq(Pay.class.getSimpleName());

        BooleanExpression conditions = condition1.and(condition2);

        List<CashLogConfirmResponse> res = jpaQueryFactory
                .select(Projections.constructor(CashLogConfirmResponse.class,
                        cashLog.id,
                        cashLog.eventType,
                        cashLog.amount,
                        cashLog.member.id,
                        cashLog.orderId,
                        cashLog.createdAt
                        )
                )
                .from(cashLog)
                .where(conditions)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(order)
                .fetch();

        // count 쿼리 최적화를 위해 JPAQuery<> 값을 받아 PageableExecution
        JPAQuery<Long> totalCount = jpaQueryFactory.select(Wildcard.count)
                .from(cashLog)
                .where(conditions);

        return PageableExecutionUtils.getPage(res, pageable, totalCount::fetchOne);
    }
}
