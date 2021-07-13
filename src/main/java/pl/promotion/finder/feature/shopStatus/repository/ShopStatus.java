package pl.promotion.finder.feature.shopStatus.repository;

import lombok.*;
import pl.promotion.finder.feature.shop.repository.ShopEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShopStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private ShopEntity shop;
    private Date lastStatus;
    private Date lastWorkedStatus;
    private boolean enable;
}
