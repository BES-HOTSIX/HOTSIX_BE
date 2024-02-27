package com.example.hotsix_be.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponRecord is a Querydsl query type for CouponRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponRecord extends EntityPathBase<CouponRecord> {

    private static final long serialVersionUID = 2005035820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponRecord couponRecord = new QCouponRecord("couponRecord");

    public final EnumPath<CouponType> couponType = createEnum("couponType", CouponType.class);

    public final NumberPath<Double> discountAmount = createNumber("discountAmount", Double.class);

    public final DatePath<java.util.Date> discountDate = createDate("discountDate", java.util.Date.class);

    public final com.example.hotsix_be.hotel.entity.QHotel hotel;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCouponRecord(String variable) {
        this(CouponRecord.class, forVariable(variable), INITS);
    }

    public QCouponRecord(Path<? extends CouponRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponRecord(PathMetadata metadata, PathInits inits) {
        this(CouponRecord.class, metadata, inits);
    }

    public QCouponRecord(Class<? extends CouponRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hotel = inits.isInitialized("hotel") ? new com.example.hotsix_be.hotel.entity.QHotel(forProperty("hotel"), inits.get("hotel")) : null;
    }

}

