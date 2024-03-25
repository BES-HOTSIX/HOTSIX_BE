//package com.example.hotsix_be.payment.cashlog.controller;
//
//import com.example.hotsix_be.auth.util.Accessor;
//import com.example.hotsix_be.member.service.MemberService;
//import com.example.hotsix_be.payment.cashlog.service.CashLogService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@WebMvcTest(CashLogController.class)
//public class CashLogControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CashLogService cashLogService;
//
//    @MockBean
//    private MemberService memberService;
//
//    private final String baseUrl = "/api/v1/cashLog";
//
//    @Test
//    @DisplayName("내 캐시 내역 페이지를 조회한다.")
//    void showMyCashLogs() throws Exception {
//        // given
//        Long MemberId = 1L;
//        Accessor.member(MemberId);
//
//        // when
//
//        // then
//        mockMvc.perform(get(baseUrl + "/me")
//                .contentType(APPLICATION_JSON)
//                .content()
//    }
//}
