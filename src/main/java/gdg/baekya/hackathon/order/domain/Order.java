package gdg.baekya.hackathon.order.domain;


import gdg.baekya.hackathon.member.domain.Member;
import gdg.baekya.hackathon.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // 펀딩하고 나서 목록 조회하기 위해서
    @Column(name = "tossOrder_id", nullable = false, unique = true, updatable = false)
    @Builder.Default
    private String tossOrderId = UUID.randomUUID().toString();

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 원하는 펀딩 상품 가져오기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 원하는 값 펀딩
    private int wantedPrice;

    // 펀딩하는 시간
    @CreatedDate
    private LocalDateTime orderDate;

    // 생성자
    public static Order of(Member member, Product product, int wantedPrice) {
        return Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .product(product)
                .wantedPrice(wantedPrice)
                .build();
    }

}