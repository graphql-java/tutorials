package com.graphqljava.tutorial.bookdetails.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Author {

    private String id;

    private String firstName;

    private String lastName;

}
