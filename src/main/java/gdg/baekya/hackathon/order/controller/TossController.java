package gdg.baekya.hackathon.order.controller;

import gdg.baekya.hackathon.order.controller.request.ConfirmPaymentRequest;
import gdg.baekya.hackathon.order.service.PaymentService;
import gdg.baekya.hackathon.product.service.TossService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@Log4j2
@RestController
@RequestMapping("api/payments/toss")
@RequiredArgsConstructor
public class TossController {

    private final TossService tossService;
    private final PaymentService paymentService;

    // 리액트에서 결제한 내용 얻기
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest confirmPaymentRequest) throws Exception {

        log.info("Confirm payment request:" + confirmPaymentRequest);

        // 토스에게 결제 승인 요청
        HttpResponse<String> response = tossService.requestConfirm(confirmPaymentRequest);

        // 응답 코드 확인
        if (response.statusCode() == 200) {
            // 결제 정보 데이터베이스 저장 시도
            paymentService.savePayment(confirmPaymentRequest, response);
            return ResponseEntity.ok(response.body()); // 성공 시 결제 객체 반환
        } else {
            // 결제가 승인되지 않음
            log.info("결제 승인 실패: " + response.body().toString());
            return ResponseEntity.status(response.statusCode())
                    .body("결제 승인 실패: " + response.body());
        }
    }
}
