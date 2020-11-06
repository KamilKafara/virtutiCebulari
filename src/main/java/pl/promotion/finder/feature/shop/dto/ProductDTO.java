package pl.promotion.finder.feature.shop.dto;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@Setter
@Getter
@ToString
public class ProductDTO {
    private final String shopName;

    private String productUrl;
    private String productName;
    private String oldPrice;
    private String newPrice;
    private String amount;
    private String pictureUrl;
    private Double percentageCut;

    public ProductDTO(String shopName, String productUrl) {
        this.newPrice = "";
        this.oldPrice = "";
        this.shopName = shopName;
        this.productUrl = productUrl;
        this.amount = "empty";
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

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
        this.oldPrice = addCurrency(this.oldPrice);
        if (!ProductDTO.this.newPrice.equals("")) {
            setupPercentageCut();
        }
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
        this.newPrice = addCurrency(this.newPrice);
        if (!oldPrice.equals("")) {
            setupPercentageCut();
        }
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
            amount = amount.replaceAll(" ", "").replaceAll("\\.", ",");
            Number number = format.parse(amount.replaceAll("[^\\d].,", ""));
            BigDecimal bigDecimalNumber = (BigDecimal) number;
            return bigDecimalNumber;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new BigDecimal(0);
    }

    private void setupPercentageCut() {
        double percentage = (parse(oldPrice).doubleValue() - (parse(newPrice).doubleValue())) / (parse(oldPrice).doubleValue()) * 100;
        this.percentageCut = BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public String getOldPrice() {
        this.oldPrice = setupPrice(oldPrice);
        return this.oldPrice;
    }

    public String getNewPrice() {
        this.newPrice = setupPrice(newPrice);
        return this.newPrice;
    }

    private String addCurrency(String content) {
        if (content.contains("zł")) {
            return content;
        } else if (!content.equals("")) {
            return content + " zł";
        } else {
            return "";
        }
    }

    private String setupPrice(String price) {
        return price.replace(",", ".").replace(" ", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equal(shopName, that.shopName) &&
                Objects.equal(productUrl, that.productUrl) &&
                Objects.equal(productName, that.productName) &&
                Objects.equal(oldPrice, that.oldPrice) &&
                Objects.equal(newPrice, that.newPrice) &&
                Objects.equal(amount, that.amount) &&
                Objects.equal(pictureUrl, that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shopName, productUrl, productName, oldPrice, newPrice, amount, pictureUrl);
    }
}
