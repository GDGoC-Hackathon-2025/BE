package gdg.baekya.hackathon.product.domain;


import gdg.baekya.hackathon.board.Enum.Category;
import gdg.baekya.hackathon.board.domain.Board;
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

    // 펀딩 현재 금액
    private int nowPrice;

    // 펀딩 상품 목표 금액
    private int goalPrice;

    // 카테고리
    @Enumerated(EnumType.STRING)
    private Category category;

    // 좋아요 목록
    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<ProductReaction> reactions = new ArrayList<>();

    // 펀딩 작성 날짜
    private LocalDateTime createdAt;

    // 펀딩 종료 날짜
    private LocalDateTime endAt;


    //생성
    public static Product of(Board board,String pname, String pdesc, int price,int goalPrice, LocalDateTime createdAt, LocalDateTime endAt) {
        return Product.builder()
                .board(board)
                .pname(pname)
                .pdesc(pdesc)
                .price(price)
                .category(board.getCategory())
                .goalPrice(goalPrice)
                .createdAt(createdAt)
                .endAt(endAt)
                .build();
    }

    public void addReaction(ProductReaction reaction) {
        reactions.add(reaction);
    }
}
