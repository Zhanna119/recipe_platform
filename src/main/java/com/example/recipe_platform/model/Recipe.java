package com.example.recipe_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Recipe {
    private Long id;
    private String title;
    private String ingredients;
    private String instruction;
    private String author;
    private String recipeAuthorEmail;


}
