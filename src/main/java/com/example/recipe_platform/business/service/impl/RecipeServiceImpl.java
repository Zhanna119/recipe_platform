package com.example.recipe_platform.business.service.impl;

import com.example.recipe_platform.business.service.RecipeService;
import com.example.recipe_platform.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Override
    public List<Recipe> getAllRecipes() {
        return null;
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Recipe> editRecipe(Long id, Recipe updatedRecipe) {
        return Optional.empty();
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public void deleteRecipeById(Long id) {

    }
}
