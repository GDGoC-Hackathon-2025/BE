package gdg.baekya.hackathon.order.service;

import gdg.baekya.hackathon.order.controller.request.OrderRequest;
import gdg.baekya.hackathon.order.service.response.TossOrderResponse;

public interface OrderService {

    // 사용자가 펀딩하기
    // 토스를 위한 주문 Response 생성
    // order는 가주문 테이블이다.
    TossOrderResponse order(OrderRequest orderRequest);



}
