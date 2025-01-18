package gdg.baekya.hackathon.order.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false, unique = true)
    private String tossPaymentKey;

    // 토스내부에서 관리하는 별도의 orderId가 존재함
    @Column(nullable = false, unique = true)
    private String tossOrderId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 총 가격
    private int totalAmount;

    @Column(nullable = false)
    private String tossOrderName;

    @Column(nullable = false)
    private String tossPaymentMethod;

    @Column(nullable = false)
    private String tossPaymentStatus;

    private String requestedAt;


}