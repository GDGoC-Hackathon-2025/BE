package gdg.baekya.hackathon.order.service;

import gdg.baekya.hackathon.order.controller.request.ConfirmPaymentRequest;
import gdg.baekya.hackathon.order.domain.Order;
import gdg.baekya.hackathon.order.domain.OrderRepository;
import gdg.baekya.hackathon.order.domain.Payment;
import gdg.baekya.hackathon.order.domain.PaymentRepository;
import gdg.baekya.hackathon.order.service.response.OrderResponse;
import gdg.baekya.hackathon.order.service.response.PaymentResponse;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.domain.Product;
import gdg.baekya.hackathon.product.domain.ProductRepository;
import gdg.baekya.hackathon.product.service.response.TossPaymentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public TossPaymentResponse savePayment(ConfirmPaymentRequest confirmPaymentRequest, HttpResponse<String> response) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONObject responseBody = (JSONObject) jsonParser.parse(response.body());

        String orderId = confirmPaymentRequest.getOrderId();
        Order order = orderRepository.findByTossOrderId(orderId).orElseThrow();


        // TossPayment 객체 생성
        Payment payment = Payment.builder()
                .tossPaymentKey((String) responseBody.get("paymentKey"))
                .tossOrderId(order.getTossOrderId())
                .order(order)
                .tossPaymentMethod((String) responseBody.get("method"))
                .tossOrderName((String) responseBody.get("orderName"))
                .tossPaymentStatus((String) responseBody.get("status"))
                .totalAmount(((int) responseBody.get("totalAmount")))
                .requestedAt((String) responseBody.get("requestedAt"))
                .build();

        // 결제 정보 저장
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment saved: " + savedPayment);

        // 가져온 오더의 상품 금액을 제외한다.
        Product product = order.getProduct();
        product.addPrice(product.getPrice());
        productRepository.save(product);

        // DTO로 변환하여 리액트에 반환
        return TossPaymentResponse.from(savedPayment);
    }

    @Override
    public OrderResponse getDetails(String orderId) throws Exception {
        Order order = orderRepository.findByTossOrderId(orderId).orElseThrow();

        return OrderResponse.from(order);
    }

    @Override
    public PageResponse<PaymentResponse> findMyPayments(Long memberId, PageRequest pageRequest) {

        // 페이지 정보와 정렬 기준 설정
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("paymentId").descending());

        // 회원 ID로 결제 정보 조회
        Page<Payment> payments = paymentRepository.getMyAllPayment(memberId,pageable);

        // 결제 정보가 존재하는 경우 DTO로 변환
        List<PaymentResponse> dtoList = PaymentResponse.froms(payments.getContent());

        // 전체 결제 건수
        long total = payments.getTotalElements();

        // 페이지 응답 반환
        return new PageResponse<>(dtoList, pageRequest, total);
    }
}
