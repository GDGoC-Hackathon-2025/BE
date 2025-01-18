package gdg.baekya.hackathon.product.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentErrorResponse {

    private int code;
    private String message;
}
