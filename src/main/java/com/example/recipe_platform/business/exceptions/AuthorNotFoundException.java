package com.example.recipe_platform.business.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String author) {
        super(String.format("Author '%s' not found", author));
    }
}

