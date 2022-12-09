package pl.promotion.finder.feature.promotion.dto.combat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CombatSalesDTO {
    private String customer;
    private String date;
    private String full_date;
    private Long quantity;
}
