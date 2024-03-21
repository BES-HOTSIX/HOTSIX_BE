package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.settle.utils.SettleUt;
import com.example.hotsix_be.reservation.entity.QReservation;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Reservation> findReservationsByHotelAndCheckoutMonth(Long hotelId, int year, int month,
                                                                     Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        List<Reservation> reservations = queryFactory
                .selectFrom(reservation)
                .where(reservation.hotel.id.eq(hotelId),
                        reservation.isPaid.isTrue(),
                        reservation.checkOutDate.year().eq(year),
                        reservation.checkOutDate.month().eq(month))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(reservation)
                .where(reservation.hotel.id.eq(hotelId),
                        reservation.isPaid.isTrue(),
                        reservation.checkOutDate.year().eq(year),
                        reservation.checkOutDate.month().eq(month))
                .fetchCount();

        return new PageImpl<>(reservations, pageable, total);
    }

    @Override
    public Long calculateTotalSales(Long hotelId, int year, int month) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        return queryFactory
                .select(reservation.price.sum())
                .from(reservation)
                .where(reservation.hotel.id.eq(hotelId),
                        reservation.isPaid.isTrue(),
                        reservation.checkOutDate.year().eq(year),
                        reservation.checkOutDate.month().eq(month),
                        reservation.checkOutDate.before(LocalDate.now()))
                .fetchOne();
    }

    @Override
    public Long countCompletedReservations(Long hotelId, int year, int month) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        return queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.hotel.id.eq(hotelId),
                        reservation.isPaid.isTrue(),
                        reservation.checkOutDate.year().eq(year),
                        reservation.checkOutDate.month().eq(month),
                        reservation.checkOutDate.before(LocalDate.now()))
                .fetchOne();
    }

    @Override
    public Long sumPriceByMemberIdAndSettleDateNull(Member host) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        return queryFactory.select(reservation.price.sum())
                .from(reservation)
                .where(reservation.host.eq(host),
                        reservation.settleDate.isNull(),
                        reservation.cancelDate.isNull())
                .fetchOne();
    }

    @Override
    public Page<Reservation> findByParamsAndCancelDateNotNull(Member host, LocalDate startDate, LocalDate endDate, String settleKw, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        List<OrderSpecifier<String>> orders = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder<Reservation> orderByExpression = new PathBuilder<>(Reservation.class, "reservation");
            orders.add(new OrderSpecifier<>(direction, orderByExpression.get(prop, String.class)));
        });

        OrderSpecifier<String>[] order = orders.toArray(OrderSpecifier[]::new);

        BooleanExpression condition1 = reservation.host.eq(host);
        BooleanExpression condition2 = reservation.cancelDate.isNull();
        BooleanExpression condition3 = reservation.settleDate.between(startDate, endDate);
        BooleanExpression condition4 = switch (settleKw) {
            case "settled" -> reservation.settleDate.isNotNull();
            case "unsettled" -> reservation.settleDate.isNull();
            default -> null;
        };
        BooleanExpression condition5 = endDate.isEqual(SettleUt.getExpectedSettleDate()) ? reservation.settleDate.isNull() : null;

        BooleanExpression conditions = condition1
                .and(condition2)
                .and(condition3)
                .and(condition4)
                .or(condition5)
                .and(condition4);

        List<Reservation> reservations = queryFactory.selectFrom(reservation)
                .where(conditions)
                .orderBy(order)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory.select(Wildcard.count)
                .from(reservation)
                .where(conditions);

        return PageableExecutionUtils.getPage(reservations, pageable, totalCount::fetchOne);
    }
}
