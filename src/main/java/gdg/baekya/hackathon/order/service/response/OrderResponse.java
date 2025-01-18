package gdg.baekya.hackathon.order.service.response;

import gdg.baekya.hackathon.order.domain.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    // 오더
    private String orderId;

    // 멤버
    private String memberName;

    // 펀딩 한 아이템
    private String item;

    // 내가 진행한 펀딩 가격
    private int price;

    // 생성자
    public static OrderResponse from(Order order) {

        return OrderResponse.builder()
                .orderId(order.getTossOrderId())
                .memberName(order.getMember().getUsername())
                .item(order.getProduct().getPname())
                .price(order.getProduct().getPrice())
                .build();
    }

}
