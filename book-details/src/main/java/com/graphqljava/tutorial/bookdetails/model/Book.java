package com.graphqljava.tutorial.bookdetails.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Book {

    private String id;

    private String name;

    private Long pageCount;

    private String authorId;

}
