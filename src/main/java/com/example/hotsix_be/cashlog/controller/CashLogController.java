package com.example.hotsix_be.cashlog.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.dto.response.*;
import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.exception.CashException;
import com.example.hotsix_be.cashlog.service.CashLogService;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cashLog")
@Slf4j
public class CashLogController {
    private final CashLogService cashLogService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @GetMapping("/detail/{cashLogId}")
    public ResponseEntity<?> getTestCashLog(@PathVariable(value = "cashLogId") long id) {
        CashLog cashLog = cashLogService.findById(id).orElse(null);

        if (cashLog == null) throw new CashException(INVALID_REQUEST);

        System.out.println(id);
        TestResponse cashLogDetailResponse = TestResponse.of(cashLog);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "테스트 캐쉬로그 조회 성공", null,
                null, cashLogDetailResponse));
    }

    @GetMapping("/me") // TODO 예약 내역도 추가
    @MemberOnly
    public ResponseEntity<?> showMyCashLogs(
            @PageableDefault(size = 5) final Pageable pageable,
            @Auth final Accessor accessor
    ) {
        long memberId = accessor.getMemberId();

        Page<CashLog> cashLogs = cashLogService.findMyPageList(memberId, pageable);

        List<CashLogConfirmResponse> cashLogConfirmResponses =
                cashLogs.stream()
                        .map(CashLogConfirmResponse::of)
                        .toList();

        PageImpl<CashLogConfirmResponse> cashLogConfirmPage =
                new PageImpl<>(
                        cashLogConfirmResponses,
                        pageable,
                        cashLogs.getTotalElements());

        MyCashLogResponse myCashLogResponse = cashLogService.getMyCashLogById(
                memberId,
                cashLogConfirmPage
        );


        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "캐시 사용 내역 조회 성공", null,
                null, myCashLogResponse
        ));
    }

    // TODO 무통장 입금 시 어드민이 수리하는 형식으로 수정할 예정
    @PostMapping("/addCash")
    public ResponseEntity<?> addCash(@RequestBody AddCashRequest addCashRequest) {

        cashLogService.addCash(addCashRequest, EventType.충전__무통장입금);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "무통장 입금이 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/payByCash/{reserveId}")
    @MemberOnly
    public ResponseEntity<?> showPayByCash(
            @PathVariable(value = "reserveId") long reserveId,
            @Auth Accessor accessor
    ) {
        // 이 메소드에서 로그인한 사용자가 예약한 본인인지도 확인
        ReservationDetailResponse reservationDetailResponse = reservationService.findById(reserveId, accessor.getMemberId());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "결제창 조회 성공", null,
                null, reservationDetailResponse));
    }

    // 결제창에서 결제하기 버튼을 누를 경우 아래 메소드가 작동
    // 이미 생성되어있는 임시 예약
    @PostMapping("/payByCash/{reserveId}")
    @MemberOnly
    public ResponseEntity<?> payByCash(@PathVariable(value = "reserveId") long reserveId) {
        Reservation reservation = reservationService.findOpById(reserveId).orElseThrow(() -> new CashException(INVALID_REQUEST));

        // 이용자 결제
        CashLog cashLog = cashLogService.payByCashOnly(reservation);

        CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLog.getId());

        if (!cashLogService.canPay(reservation, reservation.getPrice())) throw new CashException(INSUFFICIENT_DEPOSIT);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예치금 결제가 완료되었습니다.", null,
                        null, cashLogIdResponse
                )
        );
    }

    // TODO cashLog 의 내용에 따라 다르게 쓰이는 범용 완료 페이지로 만들 예정
    // TODO 본인 외 접근이 불가능하도록
    @GetMapping("/{cashLogId}/confirm")
    public ResponseEntity<?> showConfirm(
            @PathVariable(value = "cashLogId") long cashLogId
    ) {
        ConfirmResponse confirmResponse = cashLogService.getConfirmRespById(cashLogId);

        // 본인이 아닐 경우 접근 불가
//        if (!accessor.getMemberId().equals(confirmResponse.getMemberId())) throw new BadRequestException(INVALID_REQUEST);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약이 완료되었습니다.", null,
                        null, confirmResponse
                )
        );
    }

    // TODO 토스페이먼츠 완성해야함
    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 체크
//        if (!cashLogService.canPay(orderId, Long.parseLong(amount))) throw new CashException(INVALID_REQUEST);

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // 내 결제위젯 연동 키 > 시크릿 키를 입력
        // TODO 테스트를 위한 시크릿키 하드코딩 (추후 시크릿에 넣기)
        String apiKey = "test_sk_ALnQvDd2VJ69jw9egBoOVMj7X41m";

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((apiKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제 승인 API 호출
        // 결제를 승인하면 결제수단에서 금액이 차감
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;

        log.debug(String.valueOf(code));
        // 결제 승인 완료
        if (isSuccess) {
            cashLogService.payByTossPayments(Long.parseLong(orderId.split("__", 2)[1]), Long.parseLong(amount));
        } else {
            throw new CashException(FAIL_APPROVE_PURCHASE);
        }

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }
}
