package com.graphqljava.tutorial.bookdetails;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> movies = Arrays.asList(
            ImmutableMap.of("id", "movie-1",
                    "name", "Lost in translation"
            ));

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getBookTitleDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            return book.get("name");
        };
    }

    public DataFetcher helloDataFetcher() {
        return dataFetchingEnvironment -> {
            return "world";
        };
    }

    public DataFetcher getMovieByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String movieId = dataFetchingEnvironment.getArgument("id");
            return movies
                    .stream()
                    .filter(book -> book.get("id").equals(movieId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher everythingDataFetcher() {
        return env -> {
            List<Object> result = new ArrayList<>();
            result.addAll(movies);
            result.addAll(books);
            return result;
        };
    }

    public TypeResolver everythingTypeResolver = env -> {
        Map<String, Object> bookOrMovie = env.getObject();
        String id = (String) bookOrMovie.get("id");
        if (id.startsWith("movie")) {
            return (GraphQLObjectType) env.getSchema().getType("Movie");
        } else {
            return (GraphQLObjectType) env.getSchema().getType("Book");
        }

    };
}
