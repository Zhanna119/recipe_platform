package com.example.recipe_platform.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(
            description = "Recipe title"
    )
    private String title;
    @Schema(
            description = "Recipe ingredients"
    )
    private String ingredients;
    @Schema(
            description = "Recipe instruction"
    )
    private String instruction;
    @Schema(
            description = "Recipe author"
    )
    private String author;
    @Schema(
            description = "Recipe Author Email"
    )
    private String email;


}
