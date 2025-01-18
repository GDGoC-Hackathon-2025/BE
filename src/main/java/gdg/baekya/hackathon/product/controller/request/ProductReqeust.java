package gdg.baekya.hackathon.product.controller.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductReqeust {

    // 민원에서 가져오기
    private Long boardId;

    // 펀딩 이름 정하기
    private String pname;

    // 펀딩 상품 설명
    private String pdesc;

    // 펀딩 상품의 1인당 가격
    private int price;

    // 펀딩 상품 목표 금액
    private int goalPrice;

    // 펀딩 사진 넣기
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 펀딩 작성 날짜
    private LocalDateTime createdAt;

    // 펀딩 종료 날짜
    private LocalDateTime endAt;
}
