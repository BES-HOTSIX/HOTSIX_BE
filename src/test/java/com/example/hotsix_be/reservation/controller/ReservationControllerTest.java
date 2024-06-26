package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.auth.MemberOnlyChecker;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationCreateResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationInfoResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(MemberOnlyChecker.class)
@WithMockUser
public class ReservationControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BearerAuthorizationExtractor bearerAuthorizationExtractor;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    private static final MemberTokens MEMBER_TOKENS = new MemberTokens("refreshToken", "Bearer accessToken");
    private static final Cookie COOKIE = new Cookie("refresh-token", MEMBER_TOKENS.getRefreshToken());
    private Member guest;
    private Member host;
    private Hotel hotel;
    private Reservation reservation;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        given(refreshTokenRepository.existsById(any())).willReturn(true);
        doNothing().when(jwtProvider).validateTokens(any());
        given(jwtProvider.getSubject(any())).willReturn("1");
    }

    @BeforeEach
    void initData() {
        guest = new Member(
                1L,
                "jkeum",
                "jkeum",
                "https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2Fc69962b0-3951-485b-b10a-5bb29576bba8%2F13bbf61e-6916-49f1-9463-89189893b049%2F304758713-ea331acd-c248-45a3-844d-289e2ce3b0d3.jpg?table=block&id=e0b9324e-40dd-4cb1-9d41-172ebba25823&spaceId=c69962b0-3951-485b-b10a-5bb29576bba8&width=2000&userId=696dc99a-391c-4325-9088-fa16896d1aa3&cache=v2"
        );
        guest.assignRole(Role.GUEST);

        host = new Member(
                2L,
                "techit",
                "techit",
                "https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2Fc69962b0-3951-485b-b10a-5bb29576bba8%2F13bbf61e-6916-49f1-9463-89189893b049%2F304758713-ea331acd-c248-45a3-844d-289e2ce3b0d3.jpg?table=block&id=e0b9324e-40dd-4cb1-9d41-172ebba25823&spaceId=c69962b0-3951-485b-b10a-5bb29576bba8&width=2000&userId=696dc99a-391c-4325-9088-fa16896d1aa3&cache=v2"
        );
        host.assignRole(Role.HOST);

        reservation = new Reservation(
                1L,
                LocalDate.of(2024, 3, 28),
                LocalDate.of(2024, 3, 30),
                null,
                null,
                2L,
                100000L,
                0L,
                false,
                "",
                hotel,
                guest,
                host,
                null
        );

        hotel = new Hotel(
                1L,
                "호텔",
                "서울시 강남구",
                "강남역 1번출구",
                10L,
                10L,
                10L,
                10L,
                List.of("헬스장"),
                "강남호텔",
                "강남호텔입니다.",
                10000L,
                0,
                List.of(),
                List.of(reservation),
                host
        );

        ReservationInfoRequest reservationInfoRequest = new ReservationInfoRequest(
                reservation.getGuests(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getPrice(),
                reservation.isPaid()
        );
        reservation.update(reservationInfoRequest, hotel);
    }

    @Test
    @DisplayName("GET 예약 상세 정보")
    public void getReservationDetail() throws Exception {
        // given
        ReservationInfoResponse response = ReservationInfoResponse.of(
                hotel,
                reservation,
                null
        );

        given(reservationService.getInfoById(anyLong(), anyLong())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/reserve/detail/{param}", 1L)
                        .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                        .cookie(COOKIE)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andDo(print()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        // 전체 응답 내용을 JsonNode로 파싱 - objData 추출 - ReservationInfoResponse 객체로 변환
        JsonNode rootNode = objectMapper.readTree(responseContent);
        JsonNode objDataNode = rootNode.path("objData");

        ReservationInfoResponse actualResponse = objectMapper.treeToValue(objDataNode, ReservationInfoResponse.class);

        assertEquals(response.getHotelNickname(), actualResponse.getHotelNickname());
        assertEquals(response.getHotelId(), actualResponse.getHotelId());
    }

    @Test
    @DisplayName("POST 숙소 예약")
    public void reserveHotel() throws Exception {
        // given
        ReservationInfoRequest request = new ReservationInfoRequest(
                4L,
                LocalDate.of(2024, 5, 3),
                LocalDate.of(2024, 5, 8),
                300000L,
                false
        );

        ReservationCreateResponse response = ReservationCreateResponse.of(
                reservation
        );

        given(reservationService.save(anyLong(), any(ReservationInfoRequest.class), anyLong())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/reserve/{hotelId}", 1L)
                        .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                        .cookie(COOKIE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andDo(print()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        // 전체 응답 내용을 JsonNode로 파싱 - objData 추출 - String으로 변환
        JsonNode rootNode = objectMapper.readTree(responseContent);
        JsonNode objDataNode = rootNode.path("objData");
        String objDataStr = objDataNode.toString();

        String responseJson = objectMapper.writeValueAsString(response);

        assertEquals(responseJson, objDataStr);
    }
}
