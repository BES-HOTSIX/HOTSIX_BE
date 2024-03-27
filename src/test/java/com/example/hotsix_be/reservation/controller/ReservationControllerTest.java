package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationInfoResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(RestDocumentationExtension.class)
public class ReservationControllerTest {
	@Autowired
	protected MockMvc mockMvc;
	@MockBean
	private ReservationService reservationService;
	@MockBean
	private JwtProvider jwtProvider;
	@MockBean
	private BearerAuthorizationExtractor bearerAuthorizationExtractor;
	@MockBean
	private RefreshTokenRepository refreshTokenRepository;
	@MockBean
	protected RestDocumentationResultHandler restDocs;
	private Member guest;
	private Member host;
	private Hotel hotel;
	private Reservation reservation;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
					  RestDocumentationContextProvider restDocumentation) {
		this.restDocs = MockMvcRestDocumentation.document("{method-name}",
				Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
				Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
				.alwaysDo(restDocs)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.build();
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
	@WithMockUser
	public void getReservationDetail() throws Exception {
		Long param = 1L;
		ReservationInfoResponse response = ReservationInfoResponse.of(
				hotel,
				reservation,
				null
		);

		// given
		given(reservationService.getInfoById(eq(param), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/reserve/detail/{param}", param).contentType(MediaType.APPLICATION_JSON));

		// then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andDo(print()).andReturn();

		resultActions.andExpect(status().isOk())
				.andDo(
						restDocs.document(
								responseFields(
										fieldWithPath("result").description("결과"),
										fieldWithPath("status").description("상태 코드"),
										fieldWithPath("success").description("성공 메시지"),
										fieldWithPath("error").description("에러 메시지"),
										fieldWithPath("listData").description("리스트 데이터"),
										fieldWithPath("objData.hotelNickname")
												.type(JsonFieldType.STRING)
												.description("숙소명"),
										fieldWithPath("objData.hotelDescription")
												.type(JsonFieldType.STRING)
												.description("숙소 설명"),
										fieldWithPath("objData.hotelPhotoUrl")
												.type(JsonFieldType.STRING)
												.description("숙소 대표 이미지 주소"),
										fieldWithPath("objData.hotelHost")
												.type(JsonFieldType.STRING)
												.description("숙소 호스트 닉네임"),
										fieldWithPath("objData.checkInDate")
												.type(JsonFieldType.STRING)
												.description("체크인 날짜"),
										fieldWithPath("objData.checkOutDate")
												.type(JsonFieldType.STRING)
												.description("체크아웃 날짜"),
										fieldWithPath("objData.createdAt")
												.type(JsonFieldType.NULL)
												.description("생성된 시간"),
										fieldWithPath("objData.cancelDate")
												.type(JsonFieldType.NULL)
												.description("예약 취소 날짜"),
										fieldWithPath("objData.numOfGuests")
												.type(JsonFieldType.NUMBER)
												.description("숙소 이용 인원"),
										fieldWithPath("objData.paidPrice")
												.type(JsonFieldType.NUMBER)
												.description("결제 금액"),
										fieldWithPath("objData.hotelId")
												.type(JsonFieldType.NUMBER)
												.description("숙소 ID"),
										fieldWithPath("objData.reviewId")
												.type(JsonFieldType.NUMBER)
												.description("작성한 리뷰 ID")
								)
						)
				);
	}
}
