package gdg.baekya.hackathon.order.service;

import gdg.baekya.hackathon.member.domain.Member;
import gdg.baekya.hackathon.member.domain.MemberRepository;
import gdg.baekya.hackathon.order.controller.request.OrderRequest;
import gdg.baekya.hackathon.order.domain.Order;
import gdg.baekya.hackathon.order.domain.OrderRepository;
import gdg.baekya.hackathon.order.service.response.TossOrderResponse;
import gdg.baekya.hackathon.product.domain.Product;
import gdg.baekya.hackathon.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public TossOrderResponse order(OrderRequest orderRequest) {

        // 멤버 가져오기
        Member member = memberRepository.findById(orderRequest.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("멤버가 존재하지 않습니다."));

        // 상품 가져오기
        Product product = productRepository.findById(orderRequest.getProductId())
                .orElseThrow(() -> new NoSuchElementException("펀딩이 존재하지 않습니다."));

        Order newOne = Order.of(member, product);
        Order order = orderRepository.save(newOne);

        return TossOrderResponse.of(order);
    }
}
