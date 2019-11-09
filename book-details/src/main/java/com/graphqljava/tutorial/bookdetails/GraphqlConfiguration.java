package com.graphqljava.tutorial.bookdetails;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration
public class GraphqlConfiguration {

    GraphqlDataFetchers graphQLDataFetchers;

    Resource[] resources;

    public GraphqlConfiguration(@Value("classpath:graphql/*.graphqls") Resource[] resources,
                                GraphqlDataFetchers graphQLDataFetchers) {
        this.resources = resources;
        this.graphQLDataFetchers = graphQLDataFetchers;
    }

    // required by the DefaultGraphQLInvocation in the graphql-java-spring-webmvc module
    @Bean
    public GraphQL graphQL() throws IOException {
        return GraphQL.newGraphQL(buildGraphQLSchema()).build();
    }

    private GraphQLSchema buildGraphQLSchema() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();

        for (Resource resource : resources) {
            typeDefinitionRegistry.merge(schemaParser.parse(resource.getFile()));
        }

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, buildRuntimeWiring());
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }


}
