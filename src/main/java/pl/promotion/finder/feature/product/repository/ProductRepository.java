package pl.promotion.finder.feature.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByProductName(String name);

    @Query(value = "SELECT id, amount, create_date, new_price, old_price, percentage_cut, picture_url, product_name, product_url, shop_name, shop_id FROM Product p " +
            "WHERE p.product_name LIKE CONCAT('%', ?1,'%') " +
            "AND CAST (p.create_date AS VARCHAR) LIKE CONCAT('%', ?2,'%') ", nativeQuery = true)
    List<Product> findProductsByProductNameAndCreateDate(String name, String currentDate);
}
