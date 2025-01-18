package gdg.baekya.hackathon.order.controller.request;


import lombok.Data;

@Data
public class OrderRequest {

    // 멤버
    private Long memberId;

    // 아이템
    private Long productId;

}

