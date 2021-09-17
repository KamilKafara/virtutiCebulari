package pl.promotion.finder.feature.product.dto;

import pl.promotion.finder.feature.shop.dto.ShopDTO;

import java.text.ParseException;

public final class ProductDTOBuilder {
    private Long id;
    private String shopName;
    private String productUrl;
    private String productName;
    private String oldPrice;
    private String newPrice;
    private String amount;
    private String pictureUrl;
    private ShopDTO shop;

    public ProductDTOBuilder() {
    }

    public static ProductDTOBuilder aProductDTO() {
        return new ProductDTOBuilder();
    }

    public ProductDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductDTOBuilder withShopName(String shopName) {
        this.shopName = shopName;
        return this;
    }

    public ProductDTOBuilder withProductUrl(String productUrl) {
        this.productUrl = productUrl;
        return this;
    }

    public ProductDTOBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductDTOBuilder withOldPrice(String oldPrice) throws ParseException {
        this.oldPrice = PriceMapper.priceFactory(oldPrice);
        return this;
    }

    public ProductDTOBuilder withNewPrice(String newPrice) throws ParseException {
        this.newPrice = PriceMapper.priceFactory(newPrice);
        return this;
    }

    public ProductDTOBuilder withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public ProductDTOBuilder withPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public ProductDTOBuilder withShop(ShopDTO shop) {
        this.shop = shop;
        return this;
    }

    public ProductDTO build() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setShopName(shopName);
        productDTO.setProductUrl(productUrl);
        productDTO.setProductName(productName);
        productDTO.setOldPrice(oldPrice);
        productDTO.setNewPrice(newPrice);
        productDTO.setAmount(amount);
        productDTO.setPictureUrl(pictureUrl);
        productDTO.setShop(shop);
        productDTO.setupPercentageCut();
        return productDTO;
    }
}
