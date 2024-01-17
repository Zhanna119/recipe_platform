package com.example.recipe_platform.business.repository;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeDAO, Long> {

    List<RecipeDAO> findByAuthor(String author);

}
