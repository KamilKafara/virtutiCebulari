package pl.promotion.finder.feature.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    private List<ProductDTO> productList;

    public ShopDTO(@NotNull String name) {
        this.name = name;
    }
}
