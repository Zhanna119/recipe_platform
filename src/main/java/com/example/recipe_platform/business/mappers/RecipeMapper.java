package com.example.recipe_platform.business.mappers;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import com.example.recipe_platform.model.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    Recipe mapFromDao(RecipeDAO recipeDAO);

    RecipeDAO mapToDao(Recipe recipe);

}