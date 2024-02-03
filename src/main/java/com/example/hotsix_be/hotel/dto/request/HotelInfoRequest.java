package com.example.hotsix_be.hotel.dto.request;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Schema(description = "숙소 등록 정보 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class HotelInfoRequest {

    @Schema(description = "숙소 유형", example = "호텔")
    @NotBlank(message = "숙소 유형을 선택해주셔야 합니다.")
    private String hotelType;

    @Schema(description = "주소", example = "서울특별시 구로구")
    @NotBlank(message = "주소는 비어 있을 수 없습니다")
    private String address;

    @Schema(description = "상세 주소", example = "땡떙아파트 301호")
    @NotBlank(message = "상세 주소는 비어 있을 수 없습니다")
    private String addressDetail;

    @Schema(description = "방 개수", example = "3")
    @NotNull(message = "방 개수는 한 개 이상이어야 합니다.")
    private Long roomCnt;

    @Schema(description = "침대 개수", example = "2")
    @NotNull(message = "침대 개수는 한 개 이상이어야 합니다.")
    private Long bedCnt;

    @Schema(description = "화장실 개수", example = "1")
    @NotNull(message = "화장실 개수는 한 개 이상이어야 합니다.")
    private Long bathroomCnt;

    @Schema(description = "수용 가능 인원", example = "6")
    @NotNull(message = "최대 인원 수는 한 개 이상이어야 합니다.")
    private Long maxPeople;

    @Schema(description = "숙소 시설 정보")
    private List<String> facility;

    @Schema(description = "숙소 별칭", example = "5성급 같은 2성급 호텔")
    @NotBlank(message = "닉네임은 비어 있을 수 없습니다.")
    private String nickname;

    @Schema(description = "숙소 설명", example = "놀러오세요~~~~")
    @NotBlank(message = "설명은 비어 있을 수 없습니다.")
    private String description;

    @Schema(description = "숙소 하루 숙박비", example = "10000")
    @NotNull(message = "가격을 설정해야 합니다.")
    private Long price;
}
