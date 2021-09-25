package pl.promotion.finder.feature.shop.service;

import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    public ShopService(ShopRepository shopRepository, ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
    }

    public List<ShopDTO> getAll() {
        return shopRepository.findAll().stream().map(shopMapper::convertToDTO).toList();
    }

    public ShopDTO getById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isEmpty()) {
            throw new NotFoundException("Shop with this id " + id + " not found", new FieldInfo("id", ErrorCode.NOT_FOUND));
        }
        return shopMapper.convertToDTO(shop.get());
    }

    public ShopDTO getByName(String name) {
        Optional<Shop> shop = Optional.ofNullable(shopRepository.getShopByName(name));
        if (shop.isEmpty()) {
            throw new NotFoundException("Shop with this name " + name + " not found", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        return shopMapper.convertToDTO(shop.get());
    }

    public ShopDTO save(@Validated ShopDTO shopDTO) {
        log.info("save : " + shopDTO.toString());
        if (shopDTO.getId() != null) {
            throw new BadRequestException("Shop id cannot be set.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        if (shopDTO.getName() == null) {
            throw new BadRequestException("ShopDTO required name.", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        Optional<Shop> shop = Optional.ofNullable(shopRepository.getShopByName(shopDTO.getName()));
        if (shop.isPresent()) {
            throw new BadRequestException("Shop with this name " + shopDTO.getName() + " already exists", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        Shop newShop = shopMapper.convertFromDTO(shopDTO);
        return shopMapper.convertToDTO(shopRepository.save(newShop));
    }

    public ShopDTO edit(ShopDTO shopDTO, Long id) {
        log.info("edit : " + shopDTO.toString());
        if (!shopDTO.getId().equals(id)) {
            throw new BadRequestException("Shop ids is not equals.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        Shop newShop = shopRepository.save(shopMapper.convertFromDTO(shopDTO));
        return shopMapper.convertToDTO(newShop);
    }

    public void delete(Long id) {
        ShopDTO shopDTO = getById(id);
        log.info("Delete shop : " + shopDTO.toString());
        shopRepository.deleteById(shopDTO.getId());
    }
}
