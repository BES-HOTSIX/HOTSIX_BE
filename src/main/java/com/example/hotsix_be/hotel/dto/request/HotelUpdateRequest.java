package com.example.hotsix_be.hotel.dto.request;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class HotelUpdateRequest {

    @NotBlank(message = "호텔 유형을 선택해주셔야 합니다.")
    private String hotelType;

    @NotBlank(message = "주소는 비어 있을 수 없습니다")
    private String address;

    @NotBlank(message = "상세 주소는 비어 있을 수 없습니다.")
    private String addressDetail;

    @NotNull(message = "방 개수는 한 개 이상이어야 합니다.")
    private Long roomCnt;

    @NotNull(message = "침대 개수는 한 개 이상이어야 합니다.")
    private Long bedCnt;

    @NotNull(message = "욕실 개수는 한 개 이상이어야 합니다.")
    private Long bathroomCnt;

    @NotNull(message = "최대 인원 수는 한 개 이상이어야 합니다.")
    private Long maxPeople;

    private List<String> facility;

    @NotBlank(message = "닉네임은 비어 있을 수 없습니다.")
    private String nickname;

    @NotBlank(message = "설명은 비어 있을 수 없습니다.")
    private String description;

    @NotNull(message = "가격을 설정해야 합니다.")
    private Long price;
}
