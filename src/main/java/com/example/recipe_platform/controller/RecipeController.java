package com.example.recipe_platform.controller;

import com.example.recipe_platform.business.repository.RecipeRepository;
import com.example.recipe_platform.business.service.RecipeService;
import com.example.recipe_platform.model.Recipe;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    @Autowired
    RecipeService service;
    RecipeRepository repository;

    public RecipeController(RecipeService service, RecipeRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> list = service.getAllRecipes();
        if(list.isEmpty()) {
            log.warn("Recipe list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Recip-e/es found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getRecipeById(
            @NonNull @PathVariable("id") Long id) {
        Optional<Recipe> recipe = service.getRecipeById(id);
        if(recipe.isPresent()) {
            log.info("Found recipe with id {}", id);
            return ResponseEntity.ok(recipe.get());
        }
        else {
            log.warn("Recipe with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    ResponseEntity<Recipe> updateRecipe(
            @NonNull @PathVariable("id") Long id,
            @Valid @RequestBody Recipe recipe) {
        if(!recipe.getId().equals(id)) {
            log.warn("Given id {} does not match the request body or does not exists", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Recipe> recipeOptional = service.editRecipe(id, recipe);
        if(!recipeOptional.isPresent()) {
            log.warn("Recipe with given id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Recipe with id {} updated", id);
        return ResponseEntity.ok(recipeOptional.get());
    }

    @PostMapping("/save")
    ResponseEntity<Recipe> saveRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe data = service.saveRecipe(recipe);
        if(service.getRecipeById(recipe.getId()).isPresent()) {
            log.warn("Recipe with this id already exists");
            return ResponseEntity.unprocessableEntity().build();
        }
        log.info("Recipe entry saved");
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Recipe> deleteRecipeById(
            @NonNull @PathVariable("id") Long id) {
        Optional<Recipe> recipe = service.getRecipeById(id);
        if(recipe.isEmpty()){
            log.warn("Recipe with id {} is not found", id);
            return ResponseEntity.badRequest().build();
        }
        log.info("Recipe with id {} is deleted", id);
        service.deleteRecipeById(id);
        return  ResponseEntity.ok().build();
    }

    /*@GetMapping("/author/{author}")
    public ResponseEntity<List<Recipe>> getRecipesByAuthor(@NonNull @PathVariable("author") String author) {
        List<Recipe> recipesByAuthor = service.findByRecipeAuthor(author);
        if (recipesByAuthor.isEmpty()) {
            log.warn("Recipes by author {} not found", author);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Recipes found for author {}, with list size: {}", author, recipesByAuthor.size());
            return ResponseEntity.ok(recipesByAuthor);
        }
    }*/

}
