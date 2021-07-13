package pl.promotion.finder.feature.shop.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.promotion.finder.exception.BadRequestException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.ShopEntity;
import pl.promotion.finder.feature.shop.repository.ShopRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopEntityMapper shopEntityMapper;

    public ShopService(ShopRepository shopRepository, ShopEntityMapper shopEntityMapper) {
        this.shopRepository = shopRepository;
        this.shopEntityMapper = shopEntityMapper;
    }

    public List<ShopDTO> getAll() {
        return shopRepository.findAll().stream().map(shopEntityMapper::convertToDTO).collect(Collectors.toList());
    }

    public ShopDTO getById(Long id) {
        Optional<ShopEntity> shop = shopRepository.findById(id);
        if (!shop.isPresent()) {
            throw new NotFoundException("Shop with this id " + id + " not found", new FieldInfo("id", ErrorCode.NOT_FOUND));
        }
        return shopEntityMapper.convertToDTO(shop.get());
    }

    public ShopDTO getByName(String name) {
        Optional<ShopEntity> shop = Optional.ofNullable(shopRepository.getShopByName(name));
        if (!shop.isPresent()) {
            throw new NotFoundException("Shop with this name " + name + " not found", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        return shopEntityMapper.convertToDTO(shop.get());
    }

    public ShopDTO save(@Validated ShopDTO shopDTO) {
        log.info("save : " + shopDTO.toString());
        if (shopDTO.getId() != null) {
            throw new BadRequestException("Shop id cannot be set.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        if (shopDTO.getName() == null) {
            throw new BadRequestException("ShopDTO required name.", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        Optional<ShopEntity> shop = Optional.ofNullable(shopRepository.getShopByName(shopDTO.getName()));
        if (shop.isPresent()) {
            throw new BadRequestException("Shop with this name " + shopDTO.getName() + " already exists", new FieldInfo("name", ErrorCode.BAD_REQUEST));
        }
        ShopEntity newShop = shopEntityMapper.convertFromDTO(shopDTO);
        return shopEntityMapper.convertToDTO(shopRepository.save(newShop));
    }

    public ShopDTO edit(ShopDTO shopDTO, Long id) {
        log.info("edit : " + shopDTO.toString());
        if (!shopDTO.getId().equals(id)) {
            throw new BadRequestException("Shop ids is not equals.", new FieldInfo("id", ErrorCode.BAD_REQUEST));
        }
        ShopEntity newShop = shopRepository.save(shopEntityMapper.convertFromDTO(shopDTO));
        return shopEntityMapper.convertToDTO(newShop);
    }

    public void delete(Long id) {
        ShopDTO shopDTO = getById(id);
        log.info("Delete shop : " + shopDTO.toString());
        shopRepository.deleteById(shopDTO.getId());
    }
}
