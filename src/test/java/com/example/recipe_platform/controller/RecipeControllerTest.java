package com.example.recipe_platform.controller;

import com.example.recipe_platform.business.service.RecipeService;
import com.example.recipe_platform.model.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {
    @MockBean
    private RecipeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/recipes";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/edit";
    public static final String URL4 = URL + "/save";
    public static final String URL5 = URL + "/delete";
    public static final String URL6 = URL + "/author";

    private Recipe recipe;

    private List<Recipe> mockedData;

    @BeforeEach
    public void init() {
        recipe = createRecipe();
        mockedData = createMockedListRecipe();
    }

    @Test
    void testGetAllRecipes_Successful() throws Exception {
        when(service.getAllRecipes()).thenReturn(mockedData);
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(mockedData.size())))
                .andExpect(jsonPath("$[0].id").value(is(mockedData.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title").value(is(mockedData.get(0).getTitle())))
                .andExpect(jsonPath("$[0].ingredients").value(is(mockedData.get(0).getIngredients())))
                .andExpect(jsonPath("$[0].instruction").value(is(mockedData.get(0).getInstruction())))
                .andExpect(jsonPath("$[0].author").value(is(mockedData.get(0).getAuthor())))
                .andExpect(jsonPath("$[0].email").value(is(mockedData.get(0).getEmail())))
                .andExpect(jsonPath("$[1].id").value(is(mockedData.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title").value(is(mockedData.get(1).getTitle())))
                .andExpect(jsonPath("$[1].ingredients").value(is(mockedData.get(1).getIngredients())))
                .andExpect(jsonPath("$[1].instruction").value(is(mockedData.get(1).getInstruction())))
                .andExpect(jsonPath("$[1].author").value(is(mockedData.get(1).getAuthor())))
                .andExpect(jsonPath("$[1].email").value(is(mockedData.get(1).getEmail())));
        verify(service, times(1)).getAllRecipes();
    }

    @Test
    void testGetAllRecipes_EmptyList() throws Exception {
            List<Recipe> emptyList = new ArrayList<>();
        when(service.getAllRecipes()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
    }



    @Test
    void testGetRecipesById_Successful() throws Exception {
        when(service.getRecipeById(1L)).thenReturn(Optional.of(recipe));
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.ingredients", is("ingredients")))
                .andExpect(jsonPath("$.instruction", is("instruction")))
                .andExpect(jsonPath("$.author", is("author")))
                .andExpect(jsonPath("$.email", is("email")));
                verify(service, times(1)).getRecipeById(1L);
    }


    @Test
    void testGetRecipeById_NotExistingId() throws Exception {
        when(service.getRecipeById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get(URL + "/99"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getRecipeById(99L);
    }

    @Test
    void testSaveRecipe_Successful() throws Exception {
        when(service.saveRecipe(recipe)).thenReturn(recipe);
        mockMvc.perform(post(URL4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.ingredients", is("ingredients")))
                .andExpect(jsonPath("$.instruction", is("instruction")))
                .andExpect(jsonPath("$.author", is("author")))
                .andExpect(jsonPath("$.email", is("email")));
        verify(service, times(1)).saveRecipe(recipe);
    }

    @Test
    void testSaveRecipe_AccountNotExists() throws Exception {
        when(service.getRecipeById(recipe.getId())).thenReturn(Optional.of(recipe));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void testUpdateRecipe_Successful() throws Exception {
        when(service.editRecipe(1L, recipe)).thenReturn(Optional.ofNullable(recipe));
        mockMvc.perform(put(URL3 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.ingredients", is("ingredients")))
                .andExpect(jsonPath("$.instruction", is("instruction")))
                .andExpect(jsonPath("$.author", is("author")))
                .andExpect(jsonPath("$.email", is("email")));
        verify(service, times(1)).editRecipe(1L, recipe);
    }


    @Test
    void testUpdateRecipe_InvalidInput() throws Exception {
        when(service.editRecipe(2L, recipe)).thenReturn(Optional.ofNullable(recipe));
        mockMvc.perform(put(URL3 + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editRecipe(anyLong(), any());
    }


    @Test
    void testUpdateRecipe_NotFound() throws Exception {
        when(service.editRecipe(1L, recipe)).thenReturn(Optional.empty());
        mockMvc.perform(put(URL3 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editRecipe(1L, recipe);
    }


    @Test
    void testDeleteRecipeById_Successful() throws Exception{
        when(service.getRecipeById(1L)).thenReturn(Optional.of(recipe));
        mockMvc.perform(delete(URL5 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteRecipeById(1L);
    }

    @Test
    void testDeleteRecipeById_ReturnBadRequest() throws Exception{
        when(service.getRecipeById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete(URL5 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
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

    @Test
    void testGetRecipesByAuthor_Successful() throws Exception {
        String author = "JohnDoe";
        List<Recipe> recipesByAuthor = createMockedListRecipe();
        when(service.findByRecipeAuthor(author)).thenReturn(recipesByAuthor);
        mockMvc.perform(get(URL6 + "/{author}", author))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(recipesByAuthor.size())))
                .andExpect(jsonPath("$[0].id").value(is(mockedData.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title").value(is(mockedData.get(0).getTitle())))
                .andExpect(jsonPath("$[0].ingredients").value(is(mockedData.get(0).getIngredients())))
                .andExpect(jsonPath("$[0].instruction").value(is(mockedData.get(0).getInstruction())))
                .andExpect(jsonPath("$[0].author").value(is(mockedData.get(0).getAuthor())))
                .andExpect(jsonPath("$[0].email").value(is(mockedData.get(0).getEmail())))
                .andExpect(jsonPath("$[1].id").value(is(mockedData.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title").value(is(mockedData.get(1).getTitle())))
                .andExpect(jsonPath("$[1].ingredients").value(is(mockedData.get(1).getIngredients())))
                .andExpect(jsonPath("$[1].instruction").value(is(mockedData.get(1).getInstruction())))
                .andExpect(jsonPath("$[1].author").value(is(mockedData.get(1).getAuthor())))
                .andExpect(jsonPath("$[1].email").value(is(mockedData.get(1).getEmail())));
                verify(service, times(1)).findByRecipeAuthor(author);
    }

   @Test
    void testGetRecipesByAuthor_NotFound() throws Exception {
        String author = "NonExistingAuthor";
        when(service.findByRecipeAuthor(author)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL + "/author/{author}", author))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findByRecipeAuthor(author);
    }

    private List<Recipe> createMockedListRecipe() {
        List<Recipe> list = new ArrayList<>();
        list.add(new Recipe(
                1L,
                "title",
                "ingredients",
                "instruction",
                "author",
                "email"));
        list.add((new Recipe(
                2L,
                "title2",
                "ingredients2",
                "instruction2",
                "author2",
                "email2")));
        return list;
    }
}