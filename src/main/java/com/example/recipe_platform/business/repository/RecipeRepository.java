package com.example.recipe_platform.business.repository;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeDAO, Long> {
}
