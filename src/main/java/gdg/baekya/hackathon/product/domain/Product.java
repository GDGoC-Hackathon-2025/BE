package gdg.baekya.hackathon.product.domain;


import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.board.domain.BoardReaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 민원에서 펀딩 개최
    // 민원 카테고리로 가져오기
    @OneToOne
    private Board board;

    // 펀딩 상품 이름
    private String pname;

    // 펀딩 상품 설명
    private String pdesc;

    // 펀딩 상품의 가격
    private int price;

    // 펀딩 상품 목표 금액
    private int goalPrice;

    // 좋아요 목록
    @OneToMany(mappedBy = "board")
    @Builder.Default
    private List<ProductReaction> reactions = new ArrayList<>();

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

    public void addReaction(ProductReaction reaction) {
        reactions.add(reaction);
    }
}
