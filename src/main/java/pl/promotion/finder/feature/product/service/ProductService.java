package pl.promotion.finder.feature.product.service;

import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTransformer productTransformer;

    public ProductService(ProductRepository productRepository, ProductTransformer productTransformer) {
        this.productRepository = productRepository;
        this.productTransformer = productTransformer;
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
        Product product = productTransformer.convertFromDto(productDTO);
        return productTransformer.convertToDto(productRepository.save(product));
    }


}
