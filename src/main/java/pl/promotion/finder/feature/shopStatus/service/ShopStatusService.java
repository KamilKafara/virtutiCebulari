package pl.promotion.finder.feature.shopStatus.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.promotion.finder.exception.BadRequestException;
import pl.promotion.finder.exception.ErrorCode;
import pl.promotion.finder.exception.FieldInfo;
import pl.promotion.finder.exception.NotFoundException;
import pl.promotion.finder.feature.shop.dto.Shop;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.ShopEntity;
import pl.promotion.finder.feature.shop.service.ShopEntityMapper;
import pl.promotion.finder.feature.shop.service.ShopService;
import pl.promotion.finder.feature.shopStatus.dto.ShopStatusDTO;
import pl.promotion.finder.feature.shopStatus.repository.ShopStatus;
import pl.promotion.finder.feature.shopStatus.repository.ShopStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ShopStatusService {

    private final ShopStatusRepository shopStatusRepository;
    private final ShopStatusMapper shopStatusMapper;
    private final ShopService shopService;
    private final ShopEntityMapper shopEntityMapper;

    public ShopStatusService(ShopStatusRepository shopStatusRepository, ShopStatusMapper shopStatusMapper, ShopService shopService, ShopEntityMapper shopEntityMapper) {
        this.shopStatusRepository = shopStatusRepository;
        this.shopStatusMapper = shopStatusMapper;
        this.shopService = shopService;
        this.shopEntityMapper = shopEntityMapper;
    }

    public List<ShopStatusDTO> getShopsStatus() {
        return shopStatusRepository.findAll().stream()
                .map(shopStatusMapper::toShopStatusDTO)
                .collect(Collectors.toList());
    }

    public ShopStatusDTO getShopById(Long id) {
        return shopStatusRepository.findById(id).map(shopStatusMapper::toShopStatusDTO)
                .orElseThrow(() -> new NotFoundException("Not found shop status with this id:" + id,
                        new FieldInfo("id", ErrorCode.NOT_FOUND)));
    }

    public ShopStatusDTO getShopByName(Shop shop) {
        ShopDTO shopDTO = shopService.getByName(shop.name());
        ShopEntity foundedShop = shopEntityMapper.convertFromDTO(shopDTO);
        Optional<ShopStatusDTO> shopStatusDTO = Optional.ofNullable(shopStatusMapper.toShopStatusDTO(shopStatusRepository.findByShop(foundedShop)));
        return shopStatusDTO.orElseThrow(() -> new NotFoundException("Not found shop status with this name:" + shop.name(),
                new FieldInfo("name", ErrorCode.NOT_FOUND)));
    }

    public List<ShopStatusDTO> getLastEnableShopsStatus() {
        List<ShopStatus> lastShopStatuses = shopStatusRepository.findLastShopStatuses();
        return lastShopStatuses.stream()
                .map(shopStatusMapper::toShopStatusDTO)
                .collect(Collectors.toList());
    }

    public List<ShopStatusDTO> getDisableShops() {
        List<ShopStatus> lastShopStatuses = shopStatusRepository.findShopStatusesByEnableIsFalse();
        return lastShopStatuses.stream()
                .map(shopStatusMapper::toShopStatusDTO)
                .collect(Collectors.toList());
    }

    public ShopStatusDTO getLastEnableShopByName(Shop shop) {
        return getLastEnableShopsStatusByName(shop.name()).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Not found shop status with this name:" + shop.name(),
                        new FieldInfo("name", ErrorCode.NOT_FOUND)));
    }

    public List<ShopStatusDTO> getLastEnableShopsStatusByName(String shopName) {
        List<ShopStatus> lastShopStatuses = shopStatusRepository.findLastShopStatusesByShopName(shopName);
        return lastShopStatuses.stream()
                .map(shopStatusMapper::toShopStatusDTO)
                .collect(Collectors.toList());
    }

    public ShopStatusDTO save(ShopStatusDTO shopStatus) {
        if (isShopExist(shopStatus.getShop())) {
            ShopStatus addedShopStatus = shopStatusRepository.save(shopStatusMapper.fromShopStatusDTO(shopStatus));
            return shopStatusMapper.toShopStatusDTO(addedShopStatus);
        }
        log.warn("Shop already exist, ShopStatus updating processing.");
        return this.update(shopStatus, shopStatus.getId());
    }

    public ShopStatusDTO update(ShopStatusDTO shopStatus, long shopId) {
        if (isShopExist(shopStatus.getShop())) {
            if (shopStatus.getShop().getId() == shopId) {
                ShopStatus shopStatusToUpdate = shopStatusMapper.fromShopStatusDTO(shopStatus);
                shopStatusToUpdate.setId(shopId);
                ShopStatus updatedShopStatus = shopStatusRepository.save(shopStatusToUpdate);
                return shopStatusMapper.toShopStatusDTO(updatedShopStatus);
            } else {
                throw new BadRequestException("ShopStatus id's are not equals", new FieldInfo("id", ErrorCode.BAD_REQUEST));
            }
        }
        throw new NotFoundException("Not found this shopStatus: " + shopStatus.toString(), new FieldInfo("shopStatus", ErrorCode.NOT_FOUND));

    }

    private boolean isShopExist(ShopEntity shopEntity) {
        Optional<ShopStatus> shopStatus = Optional.ofNullable(shopStatusRepository.findByShop(shopEntity));
        return shopStatus.isPresent();
    }
}
