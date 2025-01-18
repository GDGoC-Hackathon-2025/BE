package gdg.baekya.hackathon.product.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    // 펀딩 상품 이름
    private String pname;

    // 펀딩 상품 설명
    private String pdesc;

    // 펀딩 상품 목표 금액
    private int goalPrice;

    // 펀딩 작성 날짜
    private LocalDateTime createdAt;

    // 펀딩 종료 날짜
    private LocalDateTime endAt;


    //생성
    public static Product of(String pname, String pdesc, int price) {
        return Product.builder()
                .pname(pname)
                .pdesc(pdesc)
                .goalPrice(price)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
