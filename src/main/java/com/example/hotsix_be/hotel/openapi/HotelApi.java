package com.example.hotsix_be.hotel.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.dto.response.HotelPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Hotel", description = "숙소 관리 API")
public interface HotelApi {

    @Operation(
            summary = "숙소 등록",
            description = "숙소 등록을 위한 API입니다. 호텔 정보는 'hotelInfo' 파트에 JSON 형식으로 제공되어야 합니다. " +
                    "다음은 'hotelInfo' JSON 객체의 예시입니다. 이 형식에 맞춰 JSON 파일을 만들어 API 테스트 시 'hotelInfo' 파트에 첨부할 수 있습니다:\n\n" +
                    "```json\n" + // JSON 예시를 강조하기 위해 Markdown 코드 블록 사용 (Swagger UI에서는 작동하지 않을 수 있음)
                    "{\n" +
                    "  \"hotelType\": \"호텔\",\n" +
                    "  \"address\": \"address\",\n" +
                    "  \"addressDetail\": \"addressDetail\",\n" +
                    "  \"roomCnt\": 0,\n" +
                    "  \"bedCnt\": 2,\n" +
                    "  \"bathroomCnt\": 3,\n" +
                    "  \"maxPeople\": 4,\n" +
                    "  \"nickname\": \"abc\",\n" +
                    "  \"description\": \"abc\",\n" +
                    "  \"price\": 10000\n" +
                    "}\n" +
                    "```\n\n" +
                    "위 형식의 JSON 데이터를 파일로 저장한 후, Swagger UI를 통한 API 테스트 시 'hotelInfo' 파트에 해당 파일을 첨부해주세요. " +
                    "'files' 파트에는 숙소의 사진 파일들을 첨부할 수 있습니다. 이 파트는 테스트에 한하여 필수가 아니며, 여러 파일을 첨부할 수 있습니다."
    )
    @ApiResponse(
            responseCode = "201",
            description = "숙소 등록 성공"
    )
    @ApiResponse(responseCode = "400", description = "숙소 등록 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> registerHotel(
            @RequestPart("hotelInfo") @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final HotelInfoRequest hotelInfoRequest,
            @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles,
            @Auth @Parameter(hidden = true) final Accessor accessor);


    @Operation(
            summary = "모든 숙소 조회",
            description = "모든 숙소 조회을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "모든 숙소 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "모든 숙소 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping
    public ResponseEntity<ResponseDto<PageImpl<HotelPageResponse>>> getHotels(
            @PageableDefault(size = 9) final Pageable pageable);


    @Operation(
            summary = "숙소 조회",
            description = "숙소 조회을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "숙소 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "숙소 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/{hotelId}")
    public ResponseEntity<ResponseDto<HotelDetailResponse>> getHotel(
            @PathVariable(value = "hotelId") final Long hotelId);


    @Operation(
            summary = "숙소 수정",
            description =
                    "숙소 정보를 수정하기 위한 API입니다. 수정할 숙소의 정보와 새로운 이미지 파일들을 제공할 수 있으며, 삭제하고자 하는 이미지의 URL을 지정할 수 있습니다.\n\n" +
                            "숙소 정보('hotelInfo')는 JSON 형식으로 제공되어야 하며, 다음은 'hotelInfo' JSON 객체의 예시입니다. 이 형식에 맞춰 JSON 파일을 만들어 API 테스트 시 'hotelInfo' 파트에 첨부할 수 있습니다:\n\n"
                            +
                            "```json\n" +
                            "{\n" +
                            "  \"hotelType\": \"리조트\",\n" +
                            "  \"address\": \"새 주소\",\n" +
                            "  \"addressDetail\": \"새 주소 상세\",\n" +
                            "  \"roomCnt\": 5,\n" +
                            "  \"bedCnt\": 10,\n" +
                            "  \"bathroomCnt\": 4,\n" +
                            "  \"maxPeople\": 20,\n" +
                            "  \"nickname\": \"새 이름\",\n" +
                            "  \"description\": \"새 설명\",\n" +
                            "  \"price\": 20000\n" +
                            "}\n" +
                            "```\n\n" +
                            "'files' 파트에는 숙소의 새로운 사진 파일들을 첨부할 수 있습니다. 'deletedImages' 파라미터를 사용하여 삭제하고자 하는 이미지의 URL 리스트를 제공할 수 있습니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "숙소 수정 성공"
    )
    @ApiResponse(responseCode = "400", description = "숙소 수정 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PutMapping(value = "/{hotelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> updateHotel(@PathVariable("hotelId") final Long hotelId,
                                                      @RequestPart("hotelInfo") @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final HotelUpdateRequest hotelUpdateRequest,
                                                      @RequestPart(value = "files", required = false) final List<MultipartFile> newImages,
                                                      @RequestParam(value = "deletedImages", required = false) final List<String> deletedImagesUrl,
                                                      @Auth @Parameter(hidden = true) final Accessor accessor
    );


    @Operation(
            summary = "숙소 삭제",
            description = "숙소 삭제을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "숙소 삭제 성공"
    )
    @ApiResponse(responseCode = "400", description = "숙소 삭제 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @DeleteMapping("/{hotelId}")
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> deleteHotel(@PathVariable("hotelId") final Long hotelId,
                                                      @Auth @Parameter(hidden = true) final Accessor accessor);

    @Operation(
            summary = "지역 및 날짜로 숙소 조회",
            description = "지역과 날짜를 기준으로 숙소를 조회하기 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "지역 및 날짜로 숙소 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "지역 및 날짜로 숙소 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @Parameter(name = "district", required = true, example = "서울")
    @Parameter(name = "startDate", required = true, example = "2024-03-06")
    @Parameter(name = "endDate", required = true, example = "2024-03-07")
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<PageImpl<HotelPageResponse>>> getHotelsByDistrictAndDateAndKw(
            @RequestParam String district,
            @RequestParam(required = false) String kw,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long bedroomCount,
            @RequestParam(required = false) Long bedCount,
            @RequestParam(required = false) Long bathroomCount,
            @RequestParam(required = false) Long maxGuestCount,
            @RequestParam(required = false) Long price,
            @PageableDefault(size = 9) Pageable pageable);

}
