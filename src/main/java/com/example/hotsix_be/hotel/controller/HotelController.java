package com.example.hotsix_be.hotel.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.dto.response.HotelPageResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.service.HotelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotels")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<?> registerHotel(@RequestPart("hotelInfo") final HotelInfoRequest hotelInfoRequest,
                                           @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles) {

        hotelService.save(hotelInfoRequest, multipartFiles);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getHotels(@PageableDefault(size = 9) final Pageable pageable) {
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
    public ResponseEntity<?> getHotel(@PathVariable(value = "hotelId") final Long hotelId) {
        HotelDetailResponse hotelDetailResponse = hotelService.findById(hotelId);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "호텔 조회 성공", null,
                null, hotelDetailResponse));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<?> updateHotel(@PathVariable("hotelId") final Long hotelId,
                                         @RequestPart("hotelInfo") final HotelUpdateRequest hotelUpdateRequest,
                                         @RequestPart(value = "files", required = false) final List<MultipartFile> newImages,
                                         @RequestParam(value = "deletedImages", required = false) List<String> deletedImagesUrl
                                         ) {

        log.info("hotelId: " + hotelId);
        log.info("hotelInfoRequest: " + hotelUpdateRequest.getNickname());
        log.info("newImages: " + newImages);
        log.info("deletedImagesJson: " + deletedImagesUrl);

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
    public ResponseEntity<?> deleteHotel(@PathVariable("hotelId") final Long hotelId) {

        hotelService.deleteHotel(hotelId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 삭제되었습니다.", null,
                        null, null
                )
        );
    }

}
