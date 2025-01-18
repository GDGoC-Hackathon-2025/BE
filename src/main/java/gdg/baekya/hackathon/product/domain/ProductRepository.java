package gdg.baekya.hackathon.product.domain;

import gdg.baekya.hackathon.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select pi from ProductImage pi join Product p on pi.product.id = :productId")
    List<ProductImage> selectProductImages(@Param("productId") Long productId);

    @Query("SELECT p, pi FROM Product p LEFT JOIN ProductImage pi ON p.id = pi.product.id WHERE pi.ord = 0 OR pi.ord IS NULL")
    Page<Object[]> selectList(Pageable pageable);

    @Query("SELECT p, pi FROM Product p " +
            "LEFT JOIN ProductImage pi ON p.id = pi.product.id " +
            "WHERE (pi.ord = 0 OR pi.ord IS NULL) AND p.category = :category")
    Page<Object[]> findByCategoryWithImages(Pageable pageable, @Param("category") Category category);

    boolean existsByBoardId(Long boardId);

}
