package com.example.recipe_platform.business.service.impl;

import com.example.recipe_platform.business.exceptions.AuthorNotFoundException;
import com.example.recipe_platform.business.exceptions.ResourceNotFoundException;
import com.example.recipe_platform.business.mappers.RecipeMapper;
import com.example.recipe_platform.business.repository.RecipeRepository;
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
                .map(recipe -> Optional.ofNullable(mapper.mapFromDao(recipe)))
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
    }


    @Override
    public Optional<Recipe> editRecipe(Long id, Recipe updatedRecipe) {
        log.info("Updating recipe entry");
        if(repository.existsById(id)){
            log.info("Recipe entry with id {} is updated", id);
            return Optional.ofNullable(mapper.mapFromDao(repository.save(mapper.mapToDao(updatedRecipe))));
        } else {
            log.warn("Recipe entry with id {} is not updated", id);
            throw new ResourceNotFoundException("Recipe", "id", id);
        }
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        log.info("Saving new recipe entry");
        return mapper.mapFromDao(repository.save(mapper.mapToDao(recipe)));
    }


    @Override
    public void deleteRecipeById(Long id) {
        log.info("Deleting recipe with id {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Recipe with id {} deleted successfully", id);
        } else {
            log.warn("Recipe with id {} not found for deletion", id);
            throw new ResourceNotFoundException("Recipe", "id", id);
        }
    }


    @Override
    public List<Recipe> findByRecipeAuthor(String author) {
        log.info("Finding recipes by author: {}", author);
        List<Recipe> recipesByAuthor = repository.findByAuthor(author)
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        if (recipesByAuthor.isEmpty()) {
            throw new AuthorNotFoundException(author);
        }
        log.info("Found {} recipes by author: {}", recipesByAuthor.size(), author);
        return recipesByAuthor;
    }
}
