package pl.promotion.finder.feature.graphQL;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;

import java.util.List;

@Component
public class AllProductDataFetcher implements DataFetcher<List<Product>> {

    private final ProductRepository productRepository;

    public AllProductDataFetcher(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return productRepository.findAll();
    }
}
