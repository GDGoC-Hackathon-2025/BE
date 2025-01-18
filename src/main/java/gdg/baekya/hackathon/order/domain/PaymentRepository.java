package gdg.baekya.hackathon.order.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p LEFT JOIN p.order o WHERE o.member.id =:memberId AND p.tossOrderId = o.tossOrderId")
    Page<Payment> getMyAllPayment(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Payment p where p.tossOrderId =:tossOrderId")
    Optional<Payment> findByTossOrderId(@Param("tossOrderId") String tossOrderId);
}
