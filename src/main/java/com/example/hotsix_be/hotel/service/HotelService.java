package com.example.hotsix_be.hotel.service;

import com.example.hotsix_be.hotel.dto.HotelInfoDto;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelNotFoundException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.service.FileService;
import java.util.HashSet;
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
    public Hotel save(final HotelInfoDto hotelInfoDto, final List<MultipartFile> multipartFiles) {

        List<Image> newImages = fileService.uploadImages(multipartFiles, "ACCOMODATION",
                hotelInfoDto.getHotelName());

        final Hotel hotel = new Hotel(hotelInfoDto.getHotelType(),
                hotelInfoDto.getHotelAddress(),
                hotelInfoDto.getHotelDetailAddress(),
                hotelInfoDto.getNumberOfBedrooms(),
                hotelInfoDto.getNumberOfBeds(),
                hotelInfoDto.getMaximumGuests(),
                hotelInfoDto.getHotelAmenities(),
                hotelInfoDto.getHotelName(),
                hotelInfoDto.getHotelDescription(),
                hotelInfoDto.getHotelPricePerNight());

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
                .orElseThrow(() -> new HotelNotFoundException("등록된 숙소가 없습니다."));
    }

}
