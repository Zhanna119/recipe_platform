package com.example.recipe_platform.business;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import com.example.recipe_platform.model.Recipe;
import org.mapstruct.Mapper;

@Mapper
public interface RecipeMapper {
    Recipe mapFromDao(RecipeDAO recipeDAO);

    RecipeDAO mapToDao(Recipe recipe);

}
