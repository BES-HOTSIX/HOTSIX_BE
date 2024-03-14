package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.QReservation;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public Long sumPriceByMemberIdAndSettleDateIsNotNull(Member host) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReservation reservation = QReservation.reservation;

        return queryFactory.select(reservation.price.sum())
                .from(reservation)
                .where(reservation.host.eq(host),
                        reservation.settleDate.isNull())
                .fetchOne();
    }
}
