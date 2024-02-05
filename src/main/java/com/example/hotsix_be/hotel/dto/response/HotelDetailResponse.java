package com.example.hotsix_be.hotel.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "숙소 상세 조회 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HotelDetailResponse {

    @Schema(description = "숙소 아이디", example = "1")
    private final Long id;

    @Schema(description = "숙소 유형", example = "호텔")
    private final String hotelType;

    @Schema(description = "주소", example = "서울특별시 구로구")
    private final String address;

    @Schema(description = "상세 주소", example = "땡떙아파트 301호")
    private final String addressDetail;

    @Schema(description = "방 개수", example = "3")
    private final Long roomCnt;

    @Schema(description = "침대 개수", example = "2")
    private final Long bedCnt;

    @Schema(description = "화장실 개수", example = "1")
    private final Long bathroomCnt;

    @Schema(description = "수용 가능 인원", example = "6")
    private final Long maxPeople;

    @Schema(description = "숙소 시설 정보", example = "TV")
    private final List<String> facility;

    @Schema(description = "숙소 별칭", example = "5성급 같은 2성급 호텔")
    private final String nickname;

    @Schema(description = "숙소 설명", example = "놀러오세요~~~~")
    private final String description;

    @Schema(description = "숙소 하루 숙박비", example = "10000")
    private final Long price;

    @Schema(description = "숙소 이미지 정보")
    private final ImagesResponse imagesResponse;

    @Schema(description = "숙소 등록 시간", example = "2023-08-15T16:34:30.388")
    private final LocalDateTime createdAt;

    @Schema(description = "숙소 등록자 닉네임", example = "닉네임")
    private final String host;

    public static HotelDetailResponse of(final Hotel hotel) {

        return new HotelDetailResponse(
                hotel.getId(),
                hotel.getHotelType(),
                hotel.getAddress(),
                hotel.getAddressDetail(),
                hotel.getRoomCnt(),
                hotel.getBedCnt(),
                hotel.getBathroomCnt(),
                hotel.getMaxPeople(),
                hotel.getFacility(),
                hotel.getNickname(),
                hotel.getDescription(),
                hotel.getPrice(),
                ImagesResponse.of(hotel.getImages()),
                hotel.getCreatedAt(),
                hotel.getOwner().getNickname()
        );
    }
}
