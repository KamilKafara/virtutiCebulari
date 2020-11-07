package pl.promotion.finder.feature.graphQL;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import pl.promotion.finder.feature.product.repository.Product;
import pl.promotion.finder.feature.product.repository.ProductRepository;

@Component
public class ProductDataFetcher implements DataFetcher<Product> {
    private final ProductRepository productRepository;

    public ProductDataFetcher(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        return productRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
