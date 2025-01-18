package gdg.baekya.hackathon.order.service;

import gdg.baekya.hackathon.order.controller.request.ConfirmPaymentRequest;
import gdg.baekya.hackathon.order.service.response.OrderResponse;
import gdg.baekya.hackathon.order.service.response.PaymentResponse;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.service.response.TossPaymentResponse;

import java.net.http.HttpResponse;

public interface PaymentService {

    // 리액트에게 결과 리턴
    TossPaymentResponse savePayment(ConfirmPaymentRequest confirmPaymentRequest , HttpResponse<String> response) throws Exception;

    // 결제에서 상세 주문정보 얻어오기
    OrderResponse getDetails(String orderId) throws Exception;

    // 내 결제 목록 가져오기
    PageResponse<PaymentResponse> findMyPayments(Long memberId, PageRequest pageRequest);

}
