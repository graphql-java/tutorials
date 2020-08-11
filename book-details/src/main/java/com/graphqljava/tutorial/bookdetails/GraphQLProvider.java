package com.graphqljava.tutorial.bookdetails;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration
public class GraphQLProvider {


    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;


    @Bean
    public GraphQL graphQL() {
        GraphQLSchema graphQLSchema = buildSchema();
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema() {
        TypeDefinitionRegistry typeRegistry = buildTypeDefinitionRegistry();
        RuntimeWiring runtimeWiring = buildWiring();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private TypeDefinitionRegistry buildTypeDefinitionRegistry(){
        InputStream sdl = ClassLoader.getSystemResourceAsStream("schema.graphqls");
        return  new SchemaParser().parse(sdl);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }


}
