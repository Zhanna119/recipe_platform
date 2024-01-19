package com.example.recipe_platform.business.service;

import com.example.recipe_platform.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(Long id);
    Optional<Recipe> editRecipe(Long id, Recipe updatedRecipe);
    Recipe saveRecipe(Recipe recipe);
    void deleteRecipeById(Long id);
    List<Recipe> findByRecipeAuthor(String author);
    //boolean isEmailExisting(String email);
}
