package com.example.hotsix_be.hotel.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.hotsix_be.hotel.dto.request.HotelInfoRequest;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.image.service.ImageService;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(MockitoExtension.class)
@Transactional
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private HotelService hotelService;

    private Member member;

    private Hotel hotel;

    @BeforeEach
    void 초기_설정() {
        member = new Member("김겸호", "123456789", "KYUMHO",
                "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                "test@test.com");
        hotel = new Hotel("호텔", "서울시 강남구", "강남역 1번출구", 10L, 10L, 10L, 10L,
                List.of("헬스장"), "강남호텔", "강남호텔입니다.", 10000L, member);
    }

    @Test
    void 숙소를_저장한다() {

        // given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes()));

        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        HotelInfoRequest hotelInfoRequest = new HotelInfoRequest("호텔", "서울시 강남구", "강남역 1번출구", 10L, 10L, 10L, 10L,
                List.of("헬스장"), "강남호텔", "강남호텔입니다.", 10000L);

        // when
        hotelService.save(hotelInfoRequest, multipartFiles, 1L);

        // then
        ArgumentCaptor<Hotel> hotelArgumentCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository, times(1)).save(hotelArgumentCaptor.capture());

        Hotel savedHotel = hotelArgumentCaptor.getValue();
        assertThat(savedHotel.getNickname()).isEqualTo(hotelInfoRequest.getNickname());
        assertThat(savedHotel.getAddress()).isEqualTo(hotelInfoRequest.getAddress());
        assertThat(savedHotel.getAddressDetail()).isEqualTo(hotelInfoRequest.getAddressDetail());
        assertThat(savedHotel.getFacility()).isEqualTo(hotelInfoRequest.getFacility());
        assertThat(savedHotel.getDescription()).isEqualTo(hotelInfoRequest.getDescription());
        assertThat(savedHotel.getPrice()).isEqualTo(hotelInfoRequest.getPrice());
        assertThat(savedHotel.getOwner()).isEqualTo(member);
    }


    @Test
    void 모든_숙소를_조회한다() {
        // given
        Pageable sortedPageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        List<Hotel> hotelList = List.of(hotel);

        Page<Hotel> hotels = new PageImpl<>(hotelList, sortedPageable, hotelList.size());

        when(hotelRepository.findAllByOrderByCreatedAtDesc(sortedPageable)).thenReturn(hotels);

        // when
        Page<Hotel> allHotels = hotelService.findPageList(sortedPageable);

        // then
        verify(hotelRepository, times(1)).findAllByOrderByCreatedAtDesc(sortedPageable);
        assertThat(allHotels).isEqualTo(hotels);
    }

    @Test
    void 지정된_아이디에_해당하는_숙소를_조회한다() {
        // given

        when(hotelRepository.findById(any(Long.class))).thenReturn(Optional.of(hotel));

        // when
        HotelDetailResponse foundHotel = hotelService.findById(1L);

        // then
        verify(hotelRepository, times(1)).findById(1L);
        assertThat(foundHotel).usingRecursiveComparison()
                .ignoringFields("imagesResponse") // imagesResponse 필드를 비교에서 제외
                .isEqualTo(HotelDetailResponse.of(hotel));
    }

    @Test
    void 숙소를_수정한다() {
        // given

        List<MultipartFile> newImages = new ArrayList<>();
        newImages.add(new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes()));

        when(hotelRepository.findById(any(Long.class))).thenReturn(Optional.of(hotel));

        HotelUpdateRequest hotelUpdateRequest = new HotelUpdateRequest("통나무집", "인천광역시", "월미도", 10L, 10L, 10L, 10L,
                List.of("헬스장"), "자연힐링", "힐링하고 가세요.", 10000L);

        // when
        hotelService.modifyHotel(1L, hotelUpdateRequest, newImages, null);

        // then
        verify(hotelRepository, times(1)).findById(1L);

        Hotel updatedHotel = hotelRepository.findById(1L).orElseThrow();

        assertThat(updatedHotel.getNickname()).isEqualTo(hotelUpdateRequest.getNickname());
        assertThat(updatedHotel.getAddress()).isEqualTo(hotelUpdateRequest.getAddress());
        assertThat(updatedHotel.getAddressDetail()).isEqualTo(hotelUpdateRequest.getAddressDetail());
        assertThat(updatedHotel.getFacility()).isEqualTo(hotelUpdateRequest.getFacility());
        assertThat(updatedHotel.getDescription()).isEqualTo(hotelUpdateRequest.getDescription());
        assertThat(updatedHotel.getPrice()).isEqualTo(hotelUpdateRequest.getPrice());
    }

    @Test
    void 숙소를_삭제한다() {
        when(hotelRepository.findById(any(Long.class))).thenReturn(Optional.of(hotel));

        // when
        hotelService.deleteHotel(1L);

        // then
        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).delete(hotel);
    }

}