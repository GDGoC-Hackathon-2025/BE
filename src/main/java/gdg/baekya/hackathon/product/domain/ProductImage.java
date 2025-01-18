package gdg.baekya.hackathon.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pimage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 파일 이름
    private String fileName;
    
    private int ord;

    //이미지 생성
    public static ProductImage of(Product product, String fileName) {
        return ProductImage.builder()
                .product(product)
                .fileName(fileName)
                .build();
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    //로직
    public void setProduct(Product product) {
        this.product = product;
    }

}
