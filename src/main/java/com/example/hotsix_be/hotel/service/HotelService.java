package com.example.hotsix_be.hotel.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.entity.ImageType;
import com.example.hotsix_be.image.service.ImageService;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_HOTEL_ID;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HotelService {

    private final ImageService imageService;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Hotel save(final HotelInfoRequest hotelInfoRequest, final List<MultipartFile> multipartFiles,
                      final Long memberId) {

        List<Image> newImages = imageService.uploadImages(multipartFiles, ImageType.ACCOMMODATION.name(),
                hotelInfoRequest.getNickname());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        memberRepository.save(member);

        final Hotel hotel = new Hotel(hotelInfoRequest.getHotelType(),
                hotelInfoRequest.getAddress(),
                hotelInfoRequest.getAddressDetail(),
                hotelInfoRequest.getRoomCnt(),
                hotelInfoRequest.getBedCnt(),
                hotelInfoRequest.getBathroomCnt(),
                hotelInfoRequest.getMaxPeople(),
                hotelInfoRequest.getFacility(),
                hotelInfoRequest.getNickname(),
                hotelInfoRequest.getDescription(),
                hotelInfoRequest.getPrice(), member);

        newImages.forEach(hotel::addImage);

        return hotelRepository.save(hotel);
    }


    @Transactional
    public void modifyHotel(final Long hotelId, final HotelUpdateRequest hotelUpdateRequest,
                            final List<MultipartFile> newImages, final List<String> deleteImagesUrl) {

        List<Image> uploadedNewImages = new ArrayList<>();

        if (newImages != null && !newImages.isEmpty()) {
            uploadedNewImages = imageService.uploadImages(newImages, "ACCOMODATION",
                    hotelUpdateRequest.getNickname());
        } // 새로운 사진이 있을 경우 업로드

        if (deleteImagesUrl != null && !deleteImagesUrl.isEmpty()) {
            deleteImages(deleteImagesUrl);
        } // 삭제된 사진이 있을 경우 삭제

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        hotel.update(hotelUpdateRequest);

        uploadedNewImages.forEach(hotel::addImage);
    }


    private void deleteImages(final List<String> deletedImagesUrl) {
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
                .orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));
    }


    public HotelDetailResponse findById(final Long id) {
        return HotelDetailResponse.of(
                hotelRepository.findById(id).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID)));
    }

    @Transactional
    public void deleteHotel(final Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
            for (Image image : hotel.getImages()) {
                String imageId = imageService.getImageIdAndDeleteImage(
                        image.getUrl()); // ImageRepository 이미지 삭제 및 S3 버켓 이미지 ID 반환
                imageService.deleteImageInS3Bucket(imageId); // S3 버켓 이미지 삭제
            }
        } // 호텔에 등록된 이미지가 있을 경우 삭제

        hotelRepository.delete(hotel);
    }

    public Page<HotelDetailResponse> findByMemberId(final Long memberId, final int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);

        return hotelRepository.findByOwnerIdOrderByIdDesc(pageable, memberId)
                .map(HotelDetailResponse::of);
    }
}
