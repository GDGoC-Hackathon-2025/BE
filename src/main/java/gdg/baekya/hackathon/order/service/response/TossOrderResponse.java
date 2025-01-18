package gdg.baekya.hackathon.order.service.response;

import gdg.baekya.hackathon.order.domain.Order;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
@Builder
public class TossOrderResponse {

    // 리액트로 넘길 토스 오더 리스폰스

    // 오더
    private String orderId;

    // 아이템
    private String orderName;
    private String amount;

    // 멤버
    private String customerName;
    private String customerEmail;
    private String customerMobilePhone;


    // 생성자
    public static TossOrderResponse of(Order order) {
        // 첫 번째 상품 외 X건 형식으로 orderName 작성
        String orderName = order.getProduct().getPname();

        // 고객 전화번호 포맷 변경
        String phoneNumber = formatPhoneNumber(order.getMember().getPhoneNumber());

        // PaymentOrderResponseDto 생성
        TossOrderResponse responseDto = TossOrderResponse.builder()
                .orderId(order.getTossOrderId()) // 주문 ID를 String으로 변환
                .orderName(orderName) // 첫번째 상품 외 X건
                .amount(String.valueOf(order.getProduct().getPrice())) // 총 금액을 String으로 변환하여 설정
                .customerName(order.getMember().getUsername()) // 고객 이름
                .customerEmail(order.getMember().getEmail()) // 고객 이메일
                .customerMobilePhone(phoneNumber) // 고객 전화번호
                .build();

        return responseDto;
    }

    // 전화번호 포맷을 "01000000000" 형식으로 맞추는 메소드
    private static String formatPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }


}
