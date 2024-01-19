package com.example.recipe_platform.business.service.impl;

import com.example.recipe_platform.business.exceptions.AuthorNotFoundException;
import com.example.recipe_platform.business.exceptions.ResourceNotFoundException;
import com.example.recipe_platform.business.mappers.RecipeMapper;
import com.example.recipe_platform.business.repository.RecipeRepository;
import com.example.recipe_platform.business.repository.model.RecipeDAO;
import com.example.recipe_platform.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    @Mock
    private RecipeRepository repository;

    @Mock
    private RecipeMapper mapper;

    @InjectMocks
    private RecipeServiceImpl service;

    private RecipeDAO recipeDAO;
    private Recipe recipe;
    private RecipeDAO oldRecipeDAOEntry;
    private List<RecipeDAO> recipeDAOList;

    @BeforeEach
    public void init() {
        recipeDAO = createRecipeDAO();
        recipe = createRecipe();
        recipeDAOList = createRecipeDAOList(recipeDAO);
        oldRecipeDAOEntry = createOldRecipeDAOEntry();

    }

    @Test
    void testGetAllRecipes_Successful() {
        when(repository.findAll()).thenReturn(recipeDAOList);
        when(mapper.mapFromDao(recipeDAO)).thenReturn(recipe);
        List<Recipe> list = service.getAllRecipes();
        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllRecipes_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Recipe> result = service.getAllRecipes();
        verify(repository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRecipeById_Successful() {
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(recipeDAO));
        when(mapper.mapFromDao(recipeDAO)).thenReturn(recipe);
        Optional<Recipe> actualResult = service.getRecipeById(id);
        assertTrue(actualResult.isPresent(), "Expected to find a recipe with ID " + id);
        assertEquals(recipe, actualResult.get(), "Retrieved recipe does not match the expected one");
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetRecipeById_NotFound() {
        long nonExistingId = 99L;
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getRecipeById(nonExistingId));
        verify(repository, times(1)).findById(nonExistingId);
    }


    @Test
    void testEditRecipe_Successful() {
        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.mapToDao(recipe)).thenReturn(recipeDAO);
        when(repository.save(recipeDAO)).thenReturn(oldRecipeDAOEntry);
        when(mapper.mapFromDao(oldRecipeDAOEntry)).thenReturn(recipe);
        Optional<Recipe> result = service.editRecipe(1L, recipe);
        assertEquals(Optional.of(recipe), result);
        verify(repository, times(1)).existsById(1L);
        verify(mapper, times(1)).mapToDao(recipe);
        verify(repository, times(1)).save(recipeDAO);
        verify(mapper, times(1)).mapFromDao(oldRecipeDAOEntry);
    }

    @Test
    void testEditRecipe_NotFound() {
        long nonExistingId = 99L;
        when(repository.existsById(nonExistingId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.editRecipe(nonExistingId, recipe),
                "Expected ResourceNotFoundException for non-existing ID " + nonExistingId);
        verify(repository, times(1)).existsById(nonExistingId);
        verify(repository, never()).save(any());
        verify(mapper, never()).mapToDao(any());
        verify(mapper, never()).mapFromDao(any());
    }


    @Test
    void testSaveRecipe_Successful() {
        when(mapper.mapToDao(recipe)).thenReturn(recipeDAO);
        when(repository.save(recipeDAO)).thenReturn(recipeDAO);
        when(mapper.mapFromDao(recipeDAO)).thenReturn(recipe);
        Recipe savedRecipe = service.saveRecipe(recipe);
        verify(mapper, times(1)).mapToDao(recipe);
        verify(repository, times(1)).save(recipeDAO);
        verify(mapper, times(1)).mapFromDao(recipeDAO);
        assertEquals(recipe, savedRecipe);
    }

    @Test
    void testSaveRecipe_SaveException() {
        when(mapper.mapToDao(recipe)).thenReturn(recipeDAO);
        when(repository.save(recipeDAO)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DataIntegrityViolationException.class, () -> service.saveRecipe(recipe),
                "Expected DataIntegrityViolationException when saving the recipe");
        verify(mapper, times(1)).mapToDao(recipe);
        verify(repository, times(1)).save(recipeDAO);
        verify(mapper, never()).mapFromDao(any());
    }



    @Test
    void testDeleteRecipeById_Successful() {
        long existingId = 1L;
        when(repository.existsById(existingId)).thenReturn(true);
        service.deleteRecipeById(existingId);
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void testDeleteRecipeById_NonExistentRecipe() {
        long nonExistingId = 99L;
        when(repository.existsById(nonExistingId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteRecipeById(nonExistingId));
        verify(repository, never()).deleteById(nonExistingId);
    }

    @Test
    void testFindByRecipeAuthor_Successful() {
        String author = "testAuthor";
        List<RecipeDAO> recipeDAOList = createRecipeDAOList(recipeDAO);
        when(repository.findByAuthor(author)).thenReturn(recipeDAOList);
        when(mapper.mapFromDao(recipeDAO)).thenReturn(recipe);
        List<Recipe> result = service.findByRecipeAuthor(author);
        assertEquals(3, result.size());
        verify(repository, times(1)).findByAuthor(author);
        verify(mapper, times(3)).mapFromDao(recipeDAO);
    }

    @Test
    void testFindByRecipeAuthor_NoRecipesFound() {
        String nonExistingAuthor = "nonexistentAuthor";
        when(repository.findByAuthor(nonExistingAuthor)).thenReturn(Collections.emptyList());
        assertThrows(AuthorNotFoundException.class, () -> service.findByRecipeAuthor(nonExistingAuthor),
                "Expected AuthorNotFoundException for non-existing author " + nonExistingAuthor);
        verify(repository, times(1)).findByAuthor(nonExistingAuthor);
        verify(mapper, never()).mapFromDao(any());
    }


    private List<RecipeDAO> createRecipeDAOList(RecipeDAO recipeDAO) {
        List<RecipeDAO> list = new ArrayList<>();
        list.add(recipeDAO);
        list.add(recipeDAO);
        list.add(recipeDAO);
        return list;
    }

    private RecipeDAO createOldRecipeDAOEntry() {
        return new RecipeDAO(
                1L,
                "titleOld",
                "ingredientsOld",
                "instructionOld",
                "authorOld",
                "emailOld"
        );
    }

    private RecipeDAO createRecipeDAO() {
        return new RecipeDAO(
                1L,
                "title",
                "ingredients",
                "instruction",
                "author",
                "email"
        );
    }

    private Recipe createRecipe() {
        return new Recipe(
                1L,
                "title",
                "ingredients",
                "instruction",
                "author",
                "email"
        );
    }
}