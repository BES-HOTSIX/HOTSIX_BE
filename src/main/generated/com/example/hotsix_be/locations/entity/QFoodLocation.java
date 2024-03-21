package com.example.hotsix_be.locations.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFoodLocation is a Querydsl query type for FoodLocation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFoodLocation extends EntityPathBase<FoodLocation> {

    private static final long serialVersionUID = -1171121636L;

    public static final QFoodLocation foodLocation = new QFoodLocation("foodLocation");

    public final QLocation _super = new QLocation(this);

    //inherited
    public final StringPath address = _super.address;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final ComparablePath<org.locationtech.jts.geom.Point> location = _super.location;

    //inherited
    public final StringPath name = _super.name;

    public QFoodLocation(String variable) {
        super(FoodLocation.class, forVariable(variable));
    }

    public QFoodLocation(Path<? extends FoodLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFoodLocation(PathMetadata metadata) {
        super(FoodLocation.class, metadata);
    }

}

