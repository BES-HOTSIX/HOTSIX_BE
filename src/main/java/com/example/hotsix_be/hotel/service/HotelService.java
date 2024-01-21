package com.example.hotsix_be.hotel.service;

import com.example.hotsix_be.hotel.dto.HotelInfoDto;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
                hotelInfoDto.getHotelPricePerNight(),
                newImages);

        return hotelRepository.save(hotel);
    }

}
