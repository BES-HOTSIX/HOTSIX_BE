package com.example.hotsix_be.payment.settle.service;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.settle.dto.response.MySettleResponse;
import com.example.hotsix_be.payment.settle.dto.response.ReservationForSettleResponse;
import com.example.hotsix_be.payment.settle.entity.Settle;
import com.example.hotsix_be.payment.settle.repository.SettleRepository;
import com.example.hotsix_be.payment.settle.utils.SettleUt;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.ALREADY_BEEN_SETTLED;
import static com.example.hotsix_be.payment.settle.utils.SettleUt.getExpectedSettleDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SettleServiceTest {
    @InjectMocks
    private SettleService settleService;

    @Mock
    private CashLogService cashLogService;

    @Mock
    private MemberService memberService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private SettleRepository settleRepository;

    @Spy
    private SettleUt settleUt;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(settleUt, "commissionRate", 10);
    }

    @Test
    @DisplayName("예약을 정산한다.")
    void doSettle() {
        // given
        Member admin = Member.builder()
                .id(1L)
                .socialLoginId("KIM")
                .nickname("KIM")
                .imageUrl(null)
                .restCash(0L)
                .build();

        Member host = Member.builder()
                .id(2L)
                .socialLoginId("techit")
                .nickname("techit")
                .imageUrl(null)
                .restCash(0L)
                .build();
        host.assignRole(Role.HOST);

        Member guest = Member.builder()
                .id(3L)
                .socialLoginId("Park")
                .nickname("Park")
                .imageUrl(null)
                .restCash(0L)
                .build();
        guest.assignRole(Role.GUEST);

        Hotel hotel = Hotel.builder()
                .hotelType("호텔")
                .address("서울시 강남구")
                .addressDetail("강남역 1번출구")
                .roomCnt(10L)
                .bedCnt(10L)
                .bathroomCnt(10L)
                .maxPeople(10L)
                .facility(List.of("헬스장"))
                .nickname("강남호텔")
                .description("강남호텔입니다.")
                .price(10000L)
                .owner(host)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(LocalDate.now().plusDays(3))
                .checkOutDate(LocalDate.now().plusDays(6))
                .guests(2L)
                .price(100000L)
                .isPaid(false)
                .hotel(hotel)
                .member(guest)
                .couponDiscountAmount(0L)
                .build();

        when(memberService.getMemberById(any())).thenReturn(admin);

        // when
        Settle result = settleService.doSettle(reservation);

        // then
        assertThat(result.getTotalAmount()).isEqualTo(reservation.getPrice());
        assertThat(admin.getRestCash()).isEqualTo(result.getCommission());
    }

    @Test
    @DisplayName("예약을 정산한다. - 실패 케이스(이미 정산된 예약)")
    void doSettle_alreadyBeenSettled() {
        // given
        Reservation reservation = Reservation.builder().settleDate(LocalDate.now()).build();

        // when
        Throwable thrown = catchThrowable(() -> settleService.doSettle(reservation));

        // then
        assertThat(thrown).isInstanceOf(PaymentException.class)
                .hasMessage(ALREADY_BEEN_SETTLED.getMessage());
    }

    @Test
    @DisplayName("Settle을 저장한다.")
    void save() {
        // given
        Settle settle = Settle.builder().build();

        when(settleRepository.save(any())).thenReturn(settle);

        // when
        settleService.save(settle);

        // then
    }

    @Test
    @DisplayName("나의 정산 정보를 가져온다.")
    void getMySettleByMemberId() {
        // given
        Member host = Member.builder()
                .id(2L)
                .socialLoginId("techit")
                .nickname("techit")
                .restCash(1000000L)
                .imageUrl(null)
                .build();
        host.assignRole(Role.HOST);

        when(memberService.getMemberById(any())).thenReturn(host);
        when(reservationService.findExpectedSettleByHost(any())).thenReturn(100000L);

        // when
        MySettleResponse result = settleService.getMySettleByMemberId(1L);

        // then
        assertThat(result.getRestCash()).isEqualTo(host.getRestCash());
        assertThat(result.getSettleDate()).isEqualTo(getExpectedSettleDate());
    }

    @Test
    @DisplayName("나의 정산 목록을 가져온다.")
    void getReserveForSettleByMemberIdAndParams() {
        // given
        Pageable pageable = PageRequest.of(0, 1);

        Member host = Member.builder()
                .id(2L)
                .socialLoginId("techit")
                .nickname("techit")
                .imageUrl(null)
                .build();
        host.assignRole(Role.HOST);

        Member guest = Member.builder()
                .id(3L)
                .socialLoginId("Park")
                .nickname("Park")
                .imageUrl(null)
                .build();
        guest.assignRole(Role.GUEST);

        Hotel hotel = Hotel.builder()
                .hotelType("호텔")
                .address("서울시 강남구")
                .addressDetail("강남역 1번출구")
                .roomCnt(10L)
                .bedCnt(10L)
                .bathroomCnt(10L)
                .maxPeople(10L)
                .facility(List.of("헬스장"))
                .nickname("강남호텔")
                .description("강남호텔입니다.")
                .price(10000L)
                .owner(host)
                .build();

        Reservation reservation = Reservation.builder()
                .checkInDate(LocalDate.now().plusDays(3))
                .checkOutDate(LocalDate.now().plusDays(6))
                .guests(2L)
                .price(100000L)
                .isPaid(false)
                .hotel(hotel)
                .member(guest)
                .build();

        when(reservationService.findByHostIdAndParamsAndCancelDateNotNull(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(reservation), pageable, 1));

        // when
        Page<ReservationForSettleResponse> result = settleService.getReserveForSettleByMemberIdAndParams(null, null, null, null, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
