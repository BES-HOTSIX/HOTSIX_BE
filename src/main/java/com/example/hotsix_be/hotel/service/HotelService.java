package com.example.hotsix_be.hotel.service;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelModifyRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.service.ImageService;
import java.util.ArrayList;
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

    private final ImageService imageService;
    private final HotelRepository hotelRepository;


    @Transactional
    public Hotel save(final HotelInfoRequest hotelInfoRequest, final List<MultipartFile> multipartFiles) {

        List<Image> newImages = imageService.uploadImages(multipartFiles, "ACCOMODATION",
                hotelInfoRequest.getHotelName());

        final Hotel hotel = new Hotel(hotelInfoRequest.getHotelType(),
                hotelInfoRequest.getHotelAddress(),
                hotelInfoRequest.getHotelDetailAddress(),
                hotelInfoRequest.getNumberOfBedrooms(),
                hotelInfoRequest.getNumberOfBeds(),
                hotelInfoRequest.getNumberOfBathrooms(),
                hotelInfoRequest.getMaximumGuests(),
                hotelInfoRequest.getHotelAmenities(),
                hotelInfoRequest.getHotelName(),
                hotelInfoRequest.getHotelDescription(),
                hotelInfoRequest.getHotelPricePerNight());

        newImages.forEach(hotel::addImage);

        return hotelRepository.save(hotel);
    }

    @Transactional
    public void modifyHotel(final Long hotelId, final HotelModifyRequest hotelModifyRequest,
                             final List<MultipartFile> newImages, final List<String> deleteImagesUrl) {

        List<Image> uploadedNewImages = new ArrayList<>();

        if (newImages != null && !newImages.isEmpty()) {
            uploadedNewImages = imageService.uploadImages(newImages, "ACCOMODATION",
                    hotelModifyRequest.getHotelName());
        } // 새로운 사진이 있을 경우 업로드

        if (deleteImagesUrl != null && !deleteImagesUrl.isEmpty()) {
            deleteImages(deleteImagesUrl);
        } // 삭제된 사진이 있을 경우 삭제

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelException(HOTEL_NOT_FOUND));

        hotel.update(hotelModifyRequest);

        uploadedNewImages.forEach(hotel::addImage);
    }

    private void deleteImages(List<String> deletedImagesUrl) {
        for (String imageUrl : deletedImagesUrl) {
            String imageId = imageService.getImageIdAndDeleteImage(
                    imageUrl); // ImageRepository 이미지 삭제 및 S3 버켓 이미지 ID 반환
            imageService.deleteImageInS3Bucket(imageId); // S3 버켓 이미지 삭제
        }
    }

    public Page<Hotel> findPageList(final Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<Hotel> pageHotel = hotelRepository.findAllByOrderByCreatedAtDesc(sortedPageable);

        return Optional.of(pageHotel)
                .filter(Slice::hasContent)
                .orElseThrow(() -> new HotelException(HOTEL_NOT_FOUND));
    }

    public HotelDetailResponse findById(Long id) {
        return HotelDetailResponse.of(
                hotelRepository.findById(id).orElseThrow(() -> new HotelException(HOTEL_NOT_FOUND)));
    }

}
