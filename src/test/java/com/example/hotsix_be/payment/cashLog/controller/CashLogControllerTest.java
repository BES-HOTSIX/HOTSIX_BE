package com.example.hotsix_be.payment.cashLog.controller;

<<<<<<< HEAD
import com.example.hotsix_be.payment.cashlog.controller.CashLogController;
import com.example.hotsix_be.payment.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CashLogController.class)
@Import(CashLogService.class)
@RequiredArgsConstructor
public class CashLogControllerTest {
    @MockBean
    CashLogRepository cashLogRepository;

    @MockBean
    ReservationRepository reservationRepository;

    private final WebTestClient webTestClient;
=======
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CashLogControllerTest {

>>>>>>> 2baeb0c1c7c28de853ccd48d7251a090ad8b28f8
}
