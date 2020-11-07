package pl.promotion.finder.feature.graphQL;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class GraphQLService {
    private final AllProductDataFetcher allProductDataFetcher;
    private final ProductDataFetcher productDataFetcher;

    @Value("classpath:schema.graphql")
    private Resource resource;

    private GraphQL graphQL;

    public GraphQLService(AllProductDataFetcher allProductDataFetcher, ProductDataFetcher productDataFetcher) {
        this.allProductDataFetcher = allProductDataFetcher;
        this.productDataFetcher = productDataFetcher;
    }

    @PostConstruct
    private void loadSchema() throws IOException {
        File file = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allProducts", allProductDataFetcher)
                        .dataFetcher("product", productDataFetcher)
                ).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
