package gdg.baekya.hackathon.product.domain;

import gdg.baekya.hackathon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReactionRepository extends JpaRepository<ProductReaction, Long> {

    // 특정 회원과 상품에 대한 좋아요 상태를 조회하는 메서드
    ProductReaction findByMemberAndProduct(Member member, Product product);

    boolean existsByMemberAndProduct(Member member, Product product);
}
