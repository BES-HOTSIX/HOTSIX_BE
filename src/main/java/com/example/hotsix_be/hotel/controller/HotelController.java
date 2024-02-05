package com.example.hotsix_be.hotel.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.dto.response.HotelPageResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.openapi.HotelApi;
import com.example.hotsix_be.hotel.service.HotelService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotels")
public class HotelController implements HotelApi {

    private final HotelService hotelService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> registerHotel(
            @RequestPart("hotelInfo") @Valid final HotelInfoRequest hotelInfoRequest,
            @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles,
            @Auth final Accessor accessor) {

        hotelService.save(hotelInfoRequest, multipartFiles, accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageImpl<HotelPageResponse>>> getHotels(
            @PageableDefault(size = 9) final Pageable pageable) {
        Page<Hotel> hotels = hotelService.findPageList(pageable);

        List<HotelPageResponse> hotelListResponse = hotels.stream().map(HotelPageResponse::of).toList();

        PageImpl<HotelPageResponse> hotelPageResponse = new PageImpl<>(hotelListResponse, pageable,
                hotels.getTotalElements());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "리스트 조회 성공", null,
                null, hotelPageResponse));
    }


    @GetMapping("/{hotelId}")
    public ResponseEntity<ResponseDto<HotelDetailResponse>> getHotel(
            @PathVariable(value = "hotelId") final Long hotelId) {
        HotelDetailResponse hotelDetailResponse = hotelService.findById(hotelId);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "호텔 조회 성공", null,
                null, hotelDetailResponse));
    }

    @PutMapping(value = "/{hotelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> updateHotel(@PathVariable("hotelId") final Long hotelId,
                                                      @RequestPart("hotelInfo") @Valid final HotelUpdateRequest hotelUpdateRequest,
                                                      @RequestPart(value = "files", required = false) final List<MultipartFile> newImages,
                                                      @RequestParam(value = "deletedImages", required = false) final List<String> deletedImagesUrl,
                                                      @Auth final Accessor accessor
    ) {

        hotelService.modifyHotel(hotelId, hotelUpdateRequest, newImages, deletedImagesUrl);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 수정되었습니다.", null,
                        null, null
                )
        );
    }

    @DeleteMapping("/{hotelId}")
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> deleteHotel(@PathVariable("hotelId") final Long hotelId,
                                                      @Auth final Accessor accessor) {

        log.info("memberId = {}", accessor.getMemberId());
        hotelService.deleteHotel(hotelId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 삭제되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<PageImpl<HotelPageResponse>>> getHotelsByDistrictAndDate(
            @RequestParam String district,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 9) Pageable pageable) {
        Page<Hotel> hotelsByDistrictAndDate = hotelService.getHotelsByDistrictAndDate(district, startDate, endDate,
                pageable);

        List<HotelPageResponse> hotelByDistrictAndDateResponse = hotelsByDistrictAndDate.stream()
                .map(HotelPageResponse::of).toList();

        PageImpl<HotelPageResponse> hotelPageResponse = new PageImpl<>(hotelByDistrictAndDateResponse, pageable,
                hotelsByDistrictAndDate.getTotalElements());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 검색이 완료되었습니다.", null,
                        null, hotelPageResponse
                )
        );
    }
}
