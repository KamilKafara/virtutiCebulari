package pl.promotion.finder.feature.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.promotion.finder.feature.product.dto.ProductDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @JsonIgnore

    private List<ProductDTO> productList;

    public ShopDTO(@NotNull String name) {
        this.name = name;
    }
}
