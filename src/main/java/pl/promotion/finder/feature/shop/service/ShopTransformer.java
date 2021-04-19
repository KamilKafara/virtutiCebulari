package pl.promotion.finder.feature.shop.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;

@Service
public class ShopTransformer {

    public Shop convertFromDTO(ShopDTO shopDTO) {
        Shop shop = new Shop();
        shop.setId(shopDTO.getId());
        shop.setName(shopDTO.getName());
        return shop;
    }

    public ShopDTO convertToDTO(Shop shop) {

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        return shopDTO;
    }
}
