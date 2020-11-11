package pl.promotion.finder.feature.shop.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;

@Service
public class ShopTransformer {

    public Shop convertFromDTO(ShopDTO shopDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(shopDTO, Shop.class);
    }

    public ShopDTO convertToDTO(Shop shop) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(shop, ShopDTO.class);
    }

}
