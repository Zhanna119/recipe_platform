package com.example.recipe_platform.business.service.impl;

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
        when(repository.findById(1L)).thenReturn(Optional.of(recipeDAO));
        when(mapper.mapFromDao(recipeDAO)).thenReturn(recipe);
        Optional<Recipe> actualResult = service.getRecipeById(recipe.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(recipe, actualResult.get());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetRecipeById_NotExistingId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Recipe> result = service.getRecipeById(99L);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(anyLong());
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
    void testEditRecipe_NotExistingId() {
        when(repository.existsById(99L)).thenReturn(false);
        Optional<Recipe> result = service.editRecipe(99L, recipe);
        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).save(any());
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
    }


    @Test
    void testSaveRecipe_DuplicateCustomer() {
        when(repository.findAll()).thenReturn(Collections.singletonList(recipeDAO));
        try {
            service.saveRecipe(recipe);
            fail("Expected IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Recipe with the same id already exists", e.getMessage());
        }
        verify(repository, times(1)).findAll();
        verify(repository, never()).save(any());
        verify(mapper, never()).mapFromDao(any());
        verify(mapper, never()).mapToDao(any());
    }


    @Test
    void testDeleteRecipeById_Successful() {
        service.deleteRecipeById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRecipeById_NonExistentCustomer() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteRecipeById(99L);
        verify(repository, times(1)).deleteById(99L);
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
        String author = "nonexistentAuthor";
        when(repository.findByAuthor(author)).thenReturn(Collections.emptyList());
        List<Recipe> result = service.findByRecipeAuthor(author);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByAuthor(author);
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