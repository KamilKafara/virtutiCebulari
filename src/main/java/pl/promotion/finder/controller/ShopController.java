package pl.promotion.finder.controller;

import graphql.ExecutionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.promotion.finder.feature.graphQL.GraphQLService;
import pl.promotion.finder.feature.shop.dto.ShopDTO;
import pl.promotion.finder.feature.shop.service.ShopService;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;
    private final GraphQLService graphQLService;

    public ShopController(ShopService shopService, GraphQLService graphQLService) {
        this.shopService = shopService;
        this.graphQLService = graphQLService;
    }

    @GetMapping
    public List<ShopDTO> getAll() {
        return shopService.getAll();
    }

    @GetMapping("/{id}")
    public ShopDTO getById(@PathVariable("id") Long id) {
        return shopService.getById(id);
    }

    @GetMapping("/graphQL/")
    @ResponseBody
    public ResponseEntity<Object> getByQuery(@RequestBody String query) {
        ExecutionResult execute = graphQLService.getGraphQL().execute(query);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
