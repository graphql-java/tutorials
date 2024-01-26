package com.graphqljava.tutorial.bookDetails;

import java.util.Arrays;
import java.util.List;

record Author(String id, String firstName, String lastName) {

    private static List<Author> authors = Arrays.asList(
        new Author("author-1", "Joanne", "Rowling"),
        new Author("author-2", "Herman", "Melville"),
        new Author("author-3", "Anne", "Rice")
    );

    public static Author getById(String id) {
        return authors.stream().filter(author -> author.id().equals(id)).findFirst().orElse(null);
    }

}