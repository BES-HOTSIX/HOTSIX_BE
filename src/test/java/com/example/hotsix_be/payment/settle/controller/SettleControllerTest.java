package com.example.hotsix_be.payment.settle.controller;

import com.example.hotsix_be.common.controller.DefaultControllerTest;
import com.example.hotsix_be.payment.settle.service.SettleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static com.amazonaws.auth.internal.SignerConstants.AUTHORIZATION;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettleController.class)
public class SettleControllerTest extends DefaultControllerTest {
    private final String baseUrl = "/api/v1/settle";

    @MockBean
    private SettleService settleService;

    private ResultActions performShowSettleInfo() throws Exception{
        return mockMvc.perform(get(baseUrl + "/me")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE));
    }

    private ResultActions performShowSettleList() throws Exception{
        return mockMvc.perform(get(baseUrl + "/me/list")
                .header(AUTHORIZATION, MEMBER_TOKENS.getAccessToken())
                .cookie(COOKIE)
                .queryParam("startDate", LocalDate.now().format(ISO_LOCAL_DATE))
                .queryParam("endDate", LocalDate.now().format(ISO_LOCAL_DATE))
                .queryParam("settleKw", "기본"));
    }

    @Test
    @DisplayName("내 정산 정보를 조회한다.")
    void showSettleInfo() throws Exception {
        // given

        // when
        final ResultActions resultActions = performShowSettleInfo();

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("내 정산 리스트를 조회한다.")
    void showSettleList() throws Exception {
        // given

        // when
        final ResultActions resultActions = performShowSettleList();

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
