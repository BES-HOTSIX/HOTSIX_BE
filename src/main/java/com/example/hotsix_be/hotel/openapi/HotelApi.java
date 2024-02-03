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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Hotel", description = "숙소 관리 API")
public interface HotelApi {

    @Operation(
            summary = "숙소 등록",
            description = "숙소 등록을 위한 API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "숙소 등록 성공",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> registerHotel(
            @RequestPart("hotelInfo") @Valid final HotelInfoRequest hotelInfoRequest,
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
    @Parameter(
            name = "pageable",
            description = "페이지네이션 정보",
            required = false
    )
    @GetMapping
    public ResponseEntity<ResponseDto<PageImpl<HotelPageResponse>>> getHotels(
            @PageableDefault(size = 9) final Pageable pageable);


    @Operation(
            summary = "숙소 조회",
            description = "숙소 조회을 위한 API"
    )
    @Parameter(
            name = "hotelId",
            description = "조회하고자 하는 호텔 id",
            required = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "숙소 조회 성공"
    )
    @GetMapping("/{hotelId}")
    public ResponseEntity<ResponseDto<HotelDetailResponse>> getHotel(
            @PathVariable(value = "hotelId") final Long hotelId);


    @Operation(
            summary = "숙소 수정",
            description = "숙소 수정을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "숙소 수정 성공"
    )
    @Parameter(
            name = "hotelId",
            description = "수정하고자 하는 호텔 id",
            required = true
    )
    @PutMapping(value = "/{hotelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> updateHotel(@PathVariable("hotelId") final Long hotelId,
                                                                  @RequestPart("hotelInfo") @Valid final HotelUpdateRequest hotelUpdateRequest,
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
    @Parameter(
            name = "hotelId",
            description = "삭제하고자 하는 호텔 id",
            required = true
    )
    @DeleteMapping("/{hotelId}")
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> deleteHotel(@PathVariable("hotelId") final Long hotelId,
                                                                  @Auth @Parameter(hidden = true) final Accessor accessor);
}
