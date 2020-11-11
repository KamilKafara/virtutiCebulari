package pl.promotion.finder.feature.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
