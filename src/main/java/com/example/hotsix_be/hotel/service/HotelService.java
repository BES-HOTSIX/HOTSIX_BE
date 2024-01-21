package com.example.hotsix_be.hotel.service;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelNotFoundException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.service.FileService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HotelService {

    private final FileService fileService;
    private final HotelRepository hotelRepository;

    @Transactional
    public Hotel save(final HotelInfoRequest hotelInfoRequest, final List<MultipartFile> multipartFiles) {

        List<Image> newImages = fileService.uploadImages(multipartFiles, "ACCOMODATION",
                hotelInfoRequest.getHotelName());

        final Hotel hotel = new Hotel(hotelInfoRequest.getHotelType(),
                hotelInfoRequest.getHotelAddress(),
                hotelInfoRequest.getHotelDetailAddress(),
                hotelInfoRequest.getNumberOfBedrooms(),
                hotelInfoRequest.getNumberOfBeds(),
                hotelInfoRequest.getMaximumGuests(),
                hotelInfoRequest.getHotelAmenities(),
                hotelInfoRequest.getHotelName(),
                hotelInfoRequest.getHotelDescription(),
                hotelInfoRequest.getHotelPricePerNight());

        newImages.forEach(hotel::addImage);

        return hotelRepository.save(hotel);
    }

    public Page<Hotel> findPageList(final Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<Hotel> pageHotel = hotelRepository.findAllByOrderByCreatedAtDesc(sortedPageable);

        return Optional.of(pageHotel)
                .filter(Slice::hasContent)
                .orElseThrow(() -> new HotelNotFoundException(HOTEL_NOT_FOUND));
    }

    public HotelDetailResponse findById(Long id) {
        return HotelDetailResponse.of(
                hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(HOTEL_NOT_FOUND)));
    }
}
