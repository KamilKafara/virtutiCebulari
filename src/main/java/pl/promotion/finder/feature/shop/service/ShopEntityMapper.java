package pl.promotion.finder.feature.shop.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.ShopEntity;

@Service
public class ShopEntityMapper {

    public ShopEntity convertFromDTO(ShopDTO shopDTO) {
        ShopEntity shop = new ShopEntity();
        shop.setId(shopDTO.getId());
        shop.setName(shopDTO.getName());
        return shop;
    }

    public ShopDTO convertToDTO(ShopEntity shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        return shopDTO;
    }
}
