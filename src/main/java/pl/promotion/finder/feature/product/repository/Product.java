package pl.promotion.finder.feature.product.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.promotion.finder.feature.shop.repository.Shop;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shopName;
    private String productUrl;
    private String productName;
    private String oldPrice;
    private String newPrice;
    private String amount;
    private String pictureUrl;
    private Double percentageCut;
    private Timestamp createDate;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Shop shop;

}
