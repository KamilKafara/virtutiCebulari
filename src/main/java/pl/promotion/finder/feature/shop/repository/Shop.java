package pl.promotion.finder.feature.shop.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.promotion.finder.feature.product.repository.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Product> products;
}
