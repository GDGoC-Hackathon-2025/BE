package gdg.baekya.hackathon.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT bc FROM Category bc WHERE bc.parent IS NULL")
    List<Category> findRootCategories();

    boolean existsByCode(String code);
}
