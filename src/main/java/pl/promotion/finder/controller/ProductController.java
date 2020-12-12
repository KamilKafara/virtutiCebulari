package pl.promotion.finder.controller;

import graphql.ExecutionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.promotion.finder.feature.graphQL.GraphQLService;
import pl.promotion.finder.feature.product.dto.ProductDTO;
import pl.promotion.finder.feature.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final GraphQLService graphQLService;

    public ProductController(ProductService productService, GraphQLService graphQLService) {
        this.productService = productService;
        this.graphQLService = graphQLService;
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @PostMapping
    private ProductDTO add(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @PostMapping(value = "/graphQL")
    @ResponseBody
    public ResponseEntity<Object> getByQuery(@RequestBody String query) {
        ExecutionResult execute = graphQLService.getGraphQL().execute(query);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }

}
