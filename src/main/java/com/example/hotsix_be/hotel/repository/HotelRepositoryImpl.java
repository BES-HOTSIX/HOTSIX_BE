package com.example.hotsix_be.hotel.repository;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.entity.QHotel;
import com.example.hotsix_be.reservation.entity.QReservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class HotelRepositoryImpl implements HotelRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Hotel> findAllByDistrictAndDate(Pageable pageable, String district, LocalDate startDate,
                                                LocalDate endDate, String kw) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QHotel hotel = QHotel.hotel;
        QReservation reservation = QReservation.reservation;

        // 해당 기간에 예약이 있는 호텔의 ID 조회
        List<Long> reservedHotelIds = queryFactory
                .select(reservation.hotel.id)
                .from(reservation)
                .where(reservation.checkInDate.before(endDate)
                        .and(reservation.checkOutDate.after(startDate)))
                .fetch();

        // 검색 조건
        BooleanExpression keywordCondition = !kw.isEmpty() ?
                hotel.nickname.contains(kw)
                        .or(hotel.description.contains(kw))
                        .or(hotel.address.contains(kw))
                        .or(hotel.addressDetail.contains(kw))
                        .or(hotel.hotelType.contains(kw)) : null;


        BooleanExpression searchCondition = hotel.address.contains(district)
                .and(hotel.id.notIn(reservedHotelIds));
        if (keywordCondition != null) {
            searchCondition = searchCondition.and(keywordCondition);
        }

        // 위에서 찾은 ID 목록에 없는 호텔들을 조회
        List<Hotel> availableHotels = queryFactory
                .selectFrom(hotel)
                .where(searchCondition)
                .orderBy(hotel.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 호텔 수 계산을 위한 쿼리 (예약되지 않은 호텔 수)
        long totalAvailableHotels = queryFactory
                .selectFrom(hotel)
                .where(searchCondition) // 검색 조건에 keywordCondition 포함
                .fetchCount();

        return new PageImpl<>(availableHotels, pageable, totalAvailableHotels);
    }
}
