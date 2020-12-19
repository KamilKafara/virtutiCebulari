package pl.promotion.finder.feature.promotion.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CombatDTO {
    private Long promotion_id;
    private boolean available;
    private Long is_current;
    private Long customer_limit;
    private String name;
    private String photo;
    private String product_url;
    private String regular_url;
    private String regular_price;
    private String promotion_price;
    private String discount;
    private Long total;
    private Long left;
    private Long sold;
    private Long percent;
    private String start_time;
    private String end_time;
    private String time_to_end;
    private List<CombatSalesDTO> sales;
    private String current_time;
    private String regulations;

}
