package pl.promotion.finder.feature.shopStatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.promotion.finder.feature.shop.repository.ShopEntity;

import java.util.List;

@Repository
public interface ShopStatusRepository extends JpaRepository<ShopStatus, Long> {
    ShopStatus findByShop(ShopEntity shop);

    @Query(value = "SELECT ss from ShopStatus as ss " +
            "JOIN ShopEntity se ON se.id = ss.shop.id " +
            "GROUP BY se.name " +
            "ORDER BY ss.lastWorkedStatus DESC ")
    List<ShopStatus> findLastShopStatuses();

    @Query(value = "SELECT ss from ShopStatus as ss " +
            "JOIN ShopEntity se ON se.id = ss.shop.id " +
            "WHERE ss.shop.name = ?1 " +
            "GROUP BY se.name " +
            "ORDER BY ss.lastWorkedStatus DESC ")
    List<ShopStatus> findLastShopStatusesByShopName(@Param("shopName") String shopName);

    List<ShopStatus> findShopStatusesByEnableIsFalse();
}
