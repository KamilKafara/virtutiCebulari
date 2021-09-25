package pl.promotion.finder.feature.graphQL;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
public class GraphQLService {
    private final AllProductDataFetcher allProductDataFetcher;
    private final ProductDataFetcher productDataFetcher;
    private static final String FILE_NAME = "/schema.graphql";

    private GraphQL graphQL;

    public GraphQLService(AllProductDataFetcher allProductDataFetcher, ProductDataFetcher productDataFetcher) {
        this.allProductDataFetcher = allProductDataFetcher;
        this.productDataFetcher = productDataFetcher;
    }

    @PostConstruct
    private void loadSchema() {
        String data = "";
        ClassPathResource resource = new ClassPathResource(FILE_NAME);
        try {
            byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
            data = new String(dataArr, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(data);
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
