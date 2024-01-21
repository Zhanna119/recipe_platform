package com.example.recipe_platform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllRecipes() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/recipes/all"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(responseJson).isInstanceOf(ArrayNode.class);
        assertThat(responseJson.size()).isEqualTo(3); // Adjust the expected length accordingly
    }


    @Test
    void testGetAllRecipesWithDetails() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/recipes/all"));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].author").value("author1"))
                .andExpect(jsonPath("$[1].author").value("author2"))
                .andExpect(jsonPath("$[0].email").value("author1@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("author2@gmail.com"))
                .andExpect(jsonPath("$[0].ingredients").value("ingredients1"))
                .andExpect(jsonPath("$[1].ingredients").value("ingredients2"));
    }

}
