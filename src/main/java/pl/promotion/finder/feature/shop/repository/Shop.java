package pl.promotion.finder.feature.shop.repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Shop {
    @Id
    private Long id;
    @NotNull
    private String name;
}
