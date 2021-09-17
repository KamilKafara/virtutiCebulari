package pl.promotion.finder.feature.product.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;
import pl.promotion.finder.feature.shop.repository.ShopRepository;
import pl.promotion.finder.feature.shop.service.ShopMapper;
import pl.promotion.finder.feature.shop.service.ShopService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductMapper productMapper;
    private final ShopMapper shopMapper;
    private final ShopService shopService;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, ProductMapper productMapper, ShopMapper shopMapper, ShopService shopService) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.productMapper = productMapper;
        this.shopMapper = shopMapper;
        this.shopService = shopService;
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream().map(productMapper::convertToDto).toList();
    }

    public List<ProductDTO> getByName(String name) {
        return productRepository.findProductsByProductName(name).stream().map(productMapper::convertToDto).toList();
    }

    public ProductDTO getProductByNameWithLowerPrice(String name) {
        Optional<Product> product = Optional.ofNullable(productRepository.findProductByProductNameWithLowerPrice(name));
        return product.map(productMapper::convertToDto).orElse(null);
    }

    public List<ProductDTO> findProductsByInterval(String name) {
        return productRepository.findProductsByProductNameAndCreateDate(name).stream().map(productMapper::convertToDto).toList();
    }

    public ProductDTO getById(Long id) {
        return productMapper.convertToDto(productRepository.getOne(id));
    }

    public ProductDTO save(ProductDTO productDTO) {
        Optional<Shop> findShop = Optional.ofNullable(shopRepository.getShopByName(productDTO.getShopName()));
        if (findShop.isEmpty()) {
            ShopDTO shopDTO = shopService.save(new ShopDTO(productDTO.getShopName()));
            productDTO.setShop(shopDTO);
        } else {
            productDTO.setShop(shopMapper.convertToDTO(findShop.get()));
        }
        Product product = productMapper.convertFromDto(productDTO);
        product.setCreateDate(new Timestamp(new Date().getTime()));
        log.info("Save product : " + productDTO);
        return productMapper.convertToDto(productRepository.save(product));
    }


}
