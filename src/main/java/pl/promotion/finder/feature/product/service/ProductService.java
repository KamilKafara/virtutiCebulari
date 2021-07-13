package pl.promotion.finder.feature.product.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.ShopEntity;
import pl.promotion.finder.feature.shop.repository.ShopRepository;
import pl.promotion.finder.feature.shop.service.ShopEntityMapper;
import pl.promotion.finder.feature.shop.service.ShopService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductMapper productMapper;
    private final ShopEntityMapper shopEntityMapper;
    private final ShopService shopService;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, ProductMapper productMapper, ShopEntityMapper shopEntityMapper, ShopService shopService) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.productMapper = productMapper;
        this.shopEntityMapper = shopEntityMapper;
        this.shopService = shopService;
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream().map(productMapper::convertToDto).collect(Collectors.toList());
    }

    public List<ProductDTO> getByName(String name) {
        return productRepository.findProductsByProductName(name).stream().map(productMapper::convertToDto).collect(Collectors.toList());
    }

    public ProductDTO getProductByNameWithLowerPrice(String name) {
        Optional<Product> product = Optional.ofNullable(productRepository.findProductByProductNameWithLowerPrice(name));
        return product.map(productMapper::convertToDto).orElse(null);
    }

    public List<ProductDTO> findProductsByInterval(String name) {
        return productRepository.findProductsByProductNameAndCreateDate(name).stream().map(productMapper::convertToDto).collect(Collectors.toList());
    }

    public ProductDTO getById(Long id) {
        return productMapper.convertToDto(productRepository.getOne(id));
    }

    public ProductDTO save(ProductDTO productDTO) {
        Optional<ShopEntity> findShop = Optional.ofNullable(shopRepository.getShopByName(productDTO.getShopName()));
        if (!findShop.isPresent()) {
            ShopDTO shopDTO = shopService.save(new ShopDTO(productDTO.getShopName()));
            productDTO.setShop(shopDTO);
        } else {
            productDTO.setShop(shopEntityMapper.convertToDTO(findShop.get()));
        }
        Product product = productMapper.convertFromDto(productDTO);
        product.setCreateDate(new Timestamp(new Date().getTime()));
        log.info("Save product : " + productDTO.toString());
        return productMapper.convertToDto(productRepository.save(product));
    }


}
