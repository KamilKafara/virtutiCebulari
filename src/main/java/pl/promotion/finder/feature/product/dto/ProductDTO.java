package pl.promotion.finder.feature.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import pl.promotion.finder.feature.shop.dto.ShopDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
@ToString
@Validated
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDTO {
    private Long id;
    @NotNull
    private String shopName;
    @NotNull
    private String productUrl;
    @NotNull
    private String productName;
    @NotNull
    private String oldPrice;
    @NotNull
    private String newPrice;
    @NotNull
    private String amount;
    @NotNull
    private String pictureUrl;
    private Double percentageCut;
    private Timestamp createDate;
    private ShopDTO shop;

    public ProductDTO() {
        this.amount = "empty";
        Date date = new Date();
        this.createDate = new Timestamp(date.getTime());
    }

    public ProductDTO(String shopName, String productUrl) {
        this.shopName = shopName;
        this.productUrl = productUrl;
        this.amount = "empty";
        Date date = new Date();
        this.createDate = new Timestamp(date.getTime());
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getPictureUrl() {
        if (this.pictureUrl == null || this.pictureUrl.equals("")) {
            try {
                this.pictureUrl = getRandomPicture();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return pictureUrl;
    }

    private String getRandomPicture() throws NoSuchAlgorithmException {
        List<String> pictures = setupPictures();

        Random rand = SecureRandom.getInstanceStrong();
        int max = pictures.size() - 1;
        int min = 0;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return pictures.get(randomNum);
    }

    private List<String> setupPictures() {
        List<String> pictures = new ArrayList<>();

        pictures.add("https://paczaizm.pl/content/wp-content/uploads/elon-musk-za-kazdym-jechanym-razem-partia-razem.jpg");
        pictures.add("https://paczaizm.pl/content/wp-content/uploads/za-kazdym-namalowanym-obrazem-adolf-hitler.jpg");
        pictures.add("https://paczaizm.pl/content/wp-content/uploads/za-kazdym-otwartym-wlazem-norek-miodowe-lata-partia-razem.jpg");
        pictures.add("https://paczaizm.pl/content/wp-content/uploads/za-kazdym-zjedzonym-plazem-bocian-partia-razem.jpg");
        pictures.add("https://www.wykop.pl/cdn/c3201142/comment_EM8D7A8huygUmdBcN4x7Mqzr2sEtN3UW.jpg");
        pictures.add("http://dev.repostuj.pl/upload/2017/07/original_156488_f209e1ed4f91ed9a839f963bbd2fcebc.jpg");
        pictures.add("https://www.wykop.pl/cdn/c3201142/comment_N9V3jpD3foMviruzynntCjHbxSr5DYgS.jpg");
        return pictures;
    }

    public Double getPercentageCut() {
        return percentageCut;
    }

    private BigDecimal parse(String amount) {
        if (amount.equals("")) {
            return new BigDecimal("0.0");
        }
        final NumberFormat format = NumberFormat.getNumberInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        try {
            amount = amount.replace(" ", "").replace("\\.", ",");
            Number number = format.parse(amount.replaceAll("[^\\d].,", ""));
            return (BigDecimal) number;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new BigDecimal("0.0");
    }

    public void setupPercentageCut() {
        if ((newPrice != null) && (oldPrice != null)) {
            double percentage = (parse(oldPrice).doubleValue() - (parse(newPrice).doubleValue())) / (parse(oldPrice).doubleValue()) * 100;
            this.percentageCut = BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {
            this.percentageCut = 0.0;
        }
    }

    public String getOldPrice() {
        return this.oldPrice;
    }

    public String getNewPrice() {
        return this.newPrice;
    }

    public boolean isFilled() {
        if (newPrice == null) {
            return false;
        }
        if (oldPrice == null) {
            return false;
        }
        if (shopName == null) {
            return false;
        }
        if (oldPrice.equals("")) {
            return false;
        }
        if (shopName.equals("")) {
            return false;
        }
        if (productUrl == null) {
            return false;
        }
        if (productName == null) {
            return false;
        }
        setupPercentageCut();
        return true;
    }
}
