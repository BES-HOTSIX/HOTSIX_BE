package com.example.hotsix_be.hotel.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.dto.HotelInfoDto;
import com.example.hotsix_be.hotel.service.HotelService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotel")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerHotel(@RequestPart("hotelInfo") HotelInfoDto hotelInfoDto,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {

        log.info(hotelInfoDto);
        log.info(multipartFiles);

        hotelService.save(hotelInfoDto, multipartFiles);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }
}
