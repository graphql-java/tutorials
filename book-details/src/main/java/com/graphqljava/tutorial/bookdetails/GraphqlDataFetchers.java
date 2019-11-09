package com.graphqljava.tutorial.bookdetails;

import graphql.schema.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Component
public class GraphqlDataFetchers {

    private final Logger logger = LoggerFactory.getLogger(GraphqlDataFetchers.class);

    public DataFetcher getBookByIdDataFetcher() {
        return env -> {
            String bookId = env.getArgument("id");
            logger.info("fetching book with id: {}", bookId);
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return env -> {
            Map<String, String> book = env.getSource();
            String authorId = book.get("authorId");
            logger.info("fetching author with id: {}", authorId);
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    private static List<Map<String, String>> books = Arrays.asList(
            Stream.of(new String[][]{
                    {"id", "book-1"},
                    {"name", "Harry Potter and the Philosopher's Stone"},
                    {"pageCount", "223"},
                    {"authorId", "author-1"}
            }).collect(toMap(data -> data[0], data -> data[1])),
            Stream.of(new String[][]{
                    {"id", "book-2"},
                    {"name", "Moby Dick"},
                    {"pageCount", "635"},
                    {"authorId", "author-2"}
            }).collect(toMap(data -> data[0], data -> data[1])),
            Stream.of(new String[][]{
                    {"id", "book-2"},
                    {"name", "Moby Dick"},
                    {"pageCount", "635"},
                    {"authorId", "author-2"}
            }).collect(toMap(data -> data[0], data -> data[1])),
            Stream.of(new String[][]{
                    {"id", "book-3"},
                    {"name", "Interview with the vampire"},
                    {"pageCount", "371"},
                    {"authorId", "author-3"}
            }).collect(toMap(data -> data[0], data -> data[1])));

    private static List<Map<String, String>> authors = Arrays.asList(
            Stream.of(new String[][]{
                    {"id", "author-1"},
                    {"firstName", "Joanne"},
                    {"lastName", "Rowling"}
            }).collect(toMap(data -> data[0], data -> data[1])),
            Stream.of(new String[][]{
                    {"id", "author-2"},
                    {"firstName", "Herman"},
                    {"lastName", "Melville"}
            }).collect(toMap(data -> data[0], data -> data[1])),
            Stream.of(new String[][]{
                    {"id", "author-3"},
                    {"firstName", "Anne"},
                    {"lastName", "Rice"}
            }).collect(toMap(data -> data[0], data -> data[1])));

}
