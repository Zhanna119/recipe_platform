package com.example.recipe_platform.business.service.impl;

import com.example.recipe_platform.business.mappers.RecipeMapper;
import com.example.recipe_platform.business.repository.RecipeRepository;
import com.example.recipe_platform.business.repository.model.RecipeDAO;
import com.example.recipe_platform.business.service.RecipeService;
import com.example.recipe_platform.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    RecipeRepository repository;

    @Autowired
    RecipeMapper mapper;


    @Override
    public List<Recipe> getAllRecipes() {
        log.info("Looking for all recipes, returning list");
        List<Recipe> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        log.info("Looking for recipe with id {}", id);
        return repository
                .findById(id)
                .flatMap(loan -> Optional.ofNullable(mapper.mapFromDao(loan)));
    }

    @Override
    public Optional<Recipe> editRecipe(Long id, Recipe updatedRecipe) {
        log.info("Updating recipe entry");
        if(repository.existsById(id)){
            log.info("Recipe entry with id {} is updated", id);
            return Optional.ofNullable(mapper.mapFromDao(repository.save(mapper.mapToDao(updatedRecipe))));
        }
        log.warn("Recipe entry with id {} is not updated", id);
        return Optional.empty();
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        List<RecipeDAO> existingRecipes = repository.findAll();
        for (RecipeDAO existingRecipe : existingRecipes) {
            if (existingRecipe.getId().equals(recipe.getId())) {
                log.warn("Recipe with the same id already exists");
                throw new IllegalArgumentException("Recipe with the same id already exists");
            }
        }
        log.info("Saving new recipe entry");
        return mapper.mapFromDao(repository.save(mapper.mapToDao(recipe)));
    }

    @Override
    public void deleteRecipeById(Long id) {
        log.info("Deleting recipe with id {}", id);
        repository.deleteById(id);
    }

   @Override
    public List<Recipe> findByRecipeAuthor(String author) {
        log.info("Finding recipes by author: {}", author);
        List<Recipe> recipesByAuthor = repository.findByAuthor(author)
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Found {} recipes by author: {}", recipesByAuthor.size(), author);
        return recipesByAuthor;
    }
}
