package gdg.baekya.hackathon.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdg.baekya.hackathon.order.controller.request.ConfirmPaymentRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Log4j2
@Service
@Transactional
public class TossServiceImpl implements TossService {

//    @Value("${payment.toss.secret_key}")
//    String widgetSecretKey;

    String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    Base64.Encoder encoder = Base64.getEncoder();
    byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
    String authorizations = "Basic " + new String(encodedBytes);

    // 오브젝트 매퍼 등록
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 토스에게 결제 승인 요청
     */
    @Override
    public HttpResponse<String> requestConfirm(ConfirmPaymentRequest confirmPaymentRequest) throws IOException, InterruptedException {
        String tossOrderId = confirmPaymentRequest.getOrderId();
        int tossAmount = confirmPaymentRequest.getAmount();
        String tossPaymentKey = confirmPaymentRequest.getPaymentKey();

        // 승인 요청에 사용할 JSON 객체를 만듭니다.
        JsonNode requestObj = objectMapper.createObjectNode()
                .put("paymentKey", tossPaymentKey)
                .put("orderId", tossOrderId)
                .put("amount", tossAmount);

        // ObjectMapper를 사용하여 JSON 객체를 문자열로 변환
        String requestBody = objectMapper.writeValueAsString(requestObj);

        // 결제 승인 API를 호출
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
//        log.info("Authorization : " + authorizations);
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    }

    /**
     * 결제 취소 요청
     */
    public HttpResponse<String> requestPaymentCancel(String paymentKey, String cancelReason) throws IOException, InterruptedException {
        System.out.println(paymentKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("cancelReason:" + cancelReason))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
}
}
