package com.example.recipe_platform.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Recipe Model Information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Recipe {
    private Long id;
    @Schema(description = "Recipe title")
    @NotEmpty(message = "Recipe title should not be null or empty")
    private String title;
    @Schema(description = "Recipe ingredients")
    @NotEmpty(message = "Recipe ingredients should not be null or empty")
    private String ingredients;
    @Schema(description = "Recipe instruction")
    @NotEmpty(message = "Recipe instruction should not be null or empty")
    private String instruction;
    @Schema(description = "Recipe author")
    @NotEmpty(message = "Recipe author should not be null or empty")
    private String author;
    @Schema(description = "Recipe Author Email")
    @NotEmpty(message = "Recipe author email should not be null or empty")
    @Email(message = "Email address should be valid")
    private String email;


}
