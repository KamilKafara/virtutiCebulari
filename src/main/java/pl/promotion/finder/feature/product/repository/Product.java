package pl.promotion.finder.feature.product.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Setter
@Getter
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

    public Product() {
        Date date = new Date();
        this.createDate = new Timestamp(date.getTime());
    }
}
