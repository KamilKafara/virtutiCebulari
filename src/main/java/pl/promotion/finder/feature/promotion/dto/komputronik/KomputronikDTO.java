package pl.promotion.finder.feature.promotion.dto.komputronik;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KomputronikDTO {
    private List<KomputronikProductDTO> products;
}
