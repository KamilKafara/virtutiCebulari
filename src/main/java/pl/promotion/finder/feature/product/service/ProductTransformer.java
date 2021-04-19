package pl.promotion.finder.feature.product.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;
import pl.promotion.finder.feature.shop.service.ShopTransformer;

@Service
@AllArgsConstructor
public class ProductTransformer {
    private final ShopTransformer shopTransformer;

    public ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setShopName(product.getShopName());
        productDTO.setProductUrl(product.getProductUrl());
        productDTO.setProductName(product.getProductName());
        productDTO.setOldPrice(product.getOldPrice());
        productDTO.setNewPrice(product.getNewPrice());
        productDTO.setAmount(product.getAmount());
        productDTO.setPictureUrl(product.getPictureUrl());
        productDTO.setPercentageCut(product.getPercentageCut());
        productDTO.setCreateDate(product.getCreateDate());

        ShopDTO shopDTO = shopTransformer.convertToDTO(product.getShop());
        productDTO.setShop(shopDTO);

        return productDTO;
    }

    public Product convertFromDto(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setShopName(productDTO.getShopName());
        product.setProductUrl(productDTO.getProductUrl());
        product.setProductName(productDTO.getProductName());
        product.setOldPrice(productDTO.getOldPrice());
        product.setNewPrice(productDTO.getNewPrice());
        product.setAmount(productDTO.getAmount());
        product.setPictureUrl(productDTO.getPictureUrl());
        product.setPercentageCut(productDTO.getPercentageCut());
        product.setCreateDate(productDTO.getCreateDate());

        Shop shop = shopTransformer.convertFromDTO(productDTO.getShop());
        product.setShop(shop);
        return product;

    }
}
