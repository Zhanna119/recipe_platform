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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {
    @MockBean
    private RecipeService service;

    @Autowired
    RecipeController controller;

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
    void testUpdateRecipe_InvalidInput() throws Exception {
        when(service.editRecipe(2L, recipe)).thenReturn(Optional.ofNullable(recipe));
        mockMvc.perform(put(URL3 + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editRecipe(anyLong(), any());
    }

    @Test
    void testSaveRecipe_AccountAlreadyExists() throws Exception {
        when(service.getRecipeById(recipe.getId())).thenReturn(Optional.of(recipe));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveRecipe_Successful() throws Exception {
        Recipe requestRecipe = new Recipe();
        requestRecipe.setTitle("Sample Title");
        requestRecipe.setIngredients("Ingredient 1, Ingredient 2");
        requestRecipe.setInstruction("Sample Instruction");
        requestRecipe.setAuthor("John Doe");
        requestRecipe.setEmail("john@example.com");
        when(service.saveRecipe(any(Recipe.class))).thenReturn(requestRecipe);
        ResultActions resultActions = mockMvc.perform(post(URL4)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestRecipe)));
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(requestRecipe.getTitle())))
                .andExpect(jsonPath("$.ingredients", is(requestRecipe.getIngredients())))
                .andExpect(jsonPath("$.instruction", is(requestRecipe.getInstruction())))
                .andExpect(jsonPath("$.author", is(requestRecipe.getAuthor())))
                .andExpect(jsonPath("$.email", is(requestRecipe.getEmail())));
        verify(service, times(1)).saveRecipe(any(Recipe.class));
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
        String author = "NonExistentAuthor";
        List<Recipe> emptyList = new ArrayList<>();
        when(service.findByRecipeAuthor(author)).thenReturn(emptyList);

        mockMvc.perform(get(URL6 + "/{author}", author))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findByRecipeAuthor(author);
    }

    @Test
    void testUpdateRecipe_Successful() throws Exception {
        Recipe updatedRecipe = new Recipe(1L, "Updated Title", "Updated Ingredients",
                "Updated Instruction", "Updated Author", "updated@email.com");
        when(service.editRecipe(eq(1L), any(Recipe.class))).thenReturn(Optional.of(updatedRecipe));
        ResultActions resultActions = mockMvc.perform(put(URL3 + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecipe)));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(updatedRecipe.getTitle())))
                .andExpect(jsonPath("$.ingredients", is(updatedRecipe.getIngredients())))
                .andExpect(jsonPath("$.instruction", is(updatedRecipe.getInstruction())))
                .andExpect(jsonPath("$.author", is(updatedRecipe.getAuthor())))
                .andExpect(jsonPath("$.email", is(updatedRecipe.getEmail())));
        verify(service).editRecipe(eq(1L), any(Recipe.class));
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



