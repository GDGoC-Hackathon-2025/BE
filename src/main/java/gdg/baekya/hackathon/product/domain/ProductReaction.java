package gdg.baekya.hackathon.product.domain;

import gdg.baekya.hackathon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private boolean liked;

    // 생성자
    // 생성자
    public static ProductReaction of(Product product, Member member) {
        ProductReaction reaction = ProductReaction.builder()
                .product(product)
                .member(member)
                .liked(true) // 초기 좋아요 상태
                .build();

        product.addReaction(reaction); // 순환 참조 방지를 위한 로직 확인 필요
        return reaction;
    }
}
