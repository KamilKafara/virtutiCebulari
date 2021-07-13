package pl.promotion.finder.feature.shopStatus.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shopStatus.dto.ShopStatusDTO;
import pl.promotion.finder.feature.shopStatus.repository.ShopStatus;

@Service
public class ShopStatusMapper {

    public ShopStatusDTO toShopStatusDTO(ShopStatus shopStatus) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(shopStatus, ShopStatusDTO.class);
    }

    public ShopStatus fromShopStatusDTO(ShopStatusDTO shopStatusDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(shopStatusDTO, ShopStatus.class);
    }

}
