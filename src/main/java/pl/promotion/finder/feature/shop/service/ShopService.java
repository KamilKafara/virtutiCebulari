package pl.promotion.finder.feature.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.promotion.finder.exception.BadRequestException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;
import pl.promotion.finder.feature.shop.repository.ShopRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopTransformer shopTransformer;

    public ShopService(ShopRepository shopRepository, ShopTransformer shopTransformer) {
        this.shopRepository = shopRepository;
        this.shopTransformer = shopTransformer;
    }

    public List<ShopDTO> getAll() {
        return shopRepository.findAll().stream().map(shopTransformer::convertToDTO).collect(Collectors.toList());
    }

    public ShopDTO getById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (!shop.isPresent()) {
            throw new NotFoundException("Shop with this id " + id + " not found", new FieldInfo("id", ErrorCode.NOT_FOUND));
        }
        return shopTransformer.convertToDTO(shop.get());
    }

    public ShopDTO getByName(String name) {
        Optional<Shop> shop = Optional.ofNullable(shopRepository.getShopByName(name));
        if (!shop.isPresent()) {
            throw new NotFoundException("Shop with this name " + name + " not found", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        return shopTransformer.convertToDTO(shop.get());
    }

    public ShopDTO save(@Validated ShopDTO shopDTO) {
        if (shopDTO.getId() != null) {
            throw new BadRequestException("Shop id cannot be set.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        Optional<Shop> shop = Optional.ofNullable(shopRepository.getShopByName(shopDTO.getName()));
        if (shop.isPresent()) {
            throw new BadRequestException("Shop with this name " + shopDTO.getName() + " already exists", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        Shop newShop = shopTransformer.convertFromDTO(shopDTO);
        return shopTransformer.convertToDTO(shopRepository.save(newShop));
    }

    public ShopDTO edit(ShopDTO shopDTO, Long id) {
        if (!shopDTO.getId().equals(id)) {
            throw new BadRequestException("Shop ids is not equals.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        Shop newShop = shopRepository.save(shopTransformer.convertFromDTO(shopDTO));
        return shopTransformer.convertToDTO(newShop);
    }

    public void delete(Long id) {
        Long shopId = getById(id).getId();
        shopRepository.deleteById(shopId);
    }
}
