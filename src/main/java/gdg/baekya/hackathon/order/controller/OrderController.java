package gdg.baekya.hackathon.order.controller;

import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.member.domain.PrincipalDetails;
import gdg.baekya.hackathon.order.controller.request.OrderRequest;
import gdg.baekya.hackathon.order.service.OrderService;
import gdg.baekya.hackathon.order.service.PaymentService;
import gdg.baekya.hackathon.order.service.response.OrderResponse;
import gdg.baekya.hackathon.order.service.response.PaymentResponse;
import gdg.baekya.hackathon.order.service.response.TossOrderResponse;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.page.request.PageRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    // 가주문 오더 생성
    @PostMapping
    public ApiResponse<TossOrderResponse> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody OrderRequest orderRequest) {
        orderRequest.setMemberId(principalDetails.getId());
        return ApiResponse.created(orderService.order(orderRequest));
    }

    // 나의 결제 내역 조회
    @GetMapping("/myOrder")
    public  ApiResponse<PageResponse<PaymentResponse>> getMyAllPayment(@AuthenticationPrincipal PrincipalDetails principalDetails, PageRequest pageRequest) {
        Long memberId = principalDetails.getId();
        return ApiResponse.ok(paymentService.findMyPayments(memberId, pageRequest));
    }

    // 토스 결제 내역으로 주문 내역 확인
    @GetMapping("/{tossOrderId}")
    public ApiResponse<OrderResponse> payment(@PathVariable("tossOrderId") String tossOrderId) throws Exception {
        return ApiResponse.ok(paymentService.getDetails(tossOrderId));
    }

}
