package pl.promotion.finder.feature.product.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.repository.Shop;
import pl.promotion.finder.feature.shop.repository.ShopRepository;
import pl.promotion.finder.feature.shop.service.ShopService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductTransformer productTransformer;
    private final ShopService shopService;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, ProductTransformer productTransformer, ShopService shopService) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.productTransformer = productTransformer;
        this.shopService = shopService;
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream().map(productTransformer::convertToDto).collect(Collectors.toList());
    }

    private List<ProductDTO> getByName(String name) {
        return productRepository.findProductsByProductName(name).stream().map(productTransformer::convertToDto).collect(Collectors.toList());
    }

    public ProductDTO getById(Long id) {
        return productTransformer.convertToDto(productRepository.getOne(id));
    }

    public ProductDTO save(ProductDTO productDTO) {
        Optional<Shop> findShop = Optional.ofNullable(shopRepository.getShopByName(productDTO.getShopName()));
        if (!findShop.isPresent()) {
            ShopDTO shopDTO = shopService.save(productDTO.getShop());
            productDTO.setShop(shopDTO);
        }
        Product product = productTransformer.convertFromDto(productDTO);
        product.setCreateDate(new Timestamp(new Date().getTime()));
        return productTransformer.convertToDto(productRepository.save(product));
    }


}
