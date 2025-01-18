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
    @JoinColumn(name = "board_id")
    private Product product;

    private int Like;

    // 생성자
    public static ProductReaction of(Product product, Member member) {
        ProductReaction reaction = ProductReaction.builder()
                .product(product)
                .member(member)
                .build();

        product.addReaction(reaction);
        return reaction;
    }
}
