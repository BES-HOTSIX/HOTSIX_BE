package com.example.hotsix_be.hotel.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHotel is a Querydsl query type for Hotel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHotel extends EntityPathBase<Hotel> {

    private static final long serialVersionUID = 1732102229L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHotel hotel = new QHotel("hotel");

    public final com.example.hotsix_be.common.entity.QDateEntity _super = new com.example.hotsix_be.common.entity.QDateEntity(this);

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final NumberPath<Long> bathroomCnt = createNumber("bathroomCnt", Long.class);

    public final NumberPath<Long> bedCnt = createNumber("bedCnt", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final ListPath<String, StringPath> facility = this.<String, StringPath>createList("facility", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath hotelType = createString("hotelType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.hotsix_be.image.entity.Image, com.example.hotsix_be.image.entity.QImage> images = this.<com.example.hotsix_be.image.entity.Image, com.example.hotsix_be.image.entity.QImage>createList("images", com.example.hotsix_be.image.entity.Image.class, com.example.hotsix_be.image.entity.QImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likesCount = createNumber("likesCount", Integer.class);

    public final NumberPath<Long> maxPeople = createNumber("maxPeople", Long.class);

    public final StringPath nickname = createString("nickname");

    public final com.example.hotsix_be.member.entity.QMember owner;

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> roomCnt = createNumber("roomCnt", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHotel(String variable) {
        this(Hotel.class, forVariable(variable), INITS);
    }

    public QHotel(Path<? extends Hotel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHotel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHotel(PathMetadata metadata, PathInits inits) {
        this(Hotel.class, metadata, inits);
    }

    public QHotel(Class<? extends Hotel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.example.hotsix_be.member.entity.QMember(forProperty("owner")) : null;
    }

}

