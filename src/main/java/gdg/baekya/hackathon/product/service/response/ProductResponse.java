package gdg.baekya.hackathon.product.service.response;

import gdg.baekya.hackathon.product.domain.Product;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@Getter
public class ProductResponse {

    private Long id;

    // 민원 id 정보
    private Long boardId;

    // 펀딩 제목
    private String pname;

    // 펀딩 설명
    private String pdesc;

    // 펀딩 가격
    private int price;

    // 펀딩 좋아요
    private int liked;

    // 펀딩 상품 목표 금액
    private int goalPrice;

    // 현재까지의 금액
    private int nowPrice;

    // 남은 금액
    private int remainPrice;

    // 펀딩 달성률 (백분율)
    private float percentage;

    // 사진 제목 가져오기
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // 엔티티를 DTO로 생성
    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .boardId(product.getBoard().getId())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .liked(product.getReactions().size())
                .goalPrice(product.getGoalPrice())
                .nowPrice(product.getNowPrice())
                .remainPrice(product.getGoalPrice() - product.getNowPrice())
                .percentage(calculatePercentage(product.getNowPrice(), product.getGoalPrice()))
                .build();
    }


    // 목표 대비 달성률 계산
    private static float calculatePercentage(int nowPrice, int goalPrice) {
        if (goalPrice == 0) {
            return 0; // 목표 금액이 0인 경우, 퍼센트 계산을 방지
        }
        return (nowPrice / (float) goalPrice);
    }
}

