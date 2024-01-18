package com.example.recipe_platform.business.repository;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import com.example.recipe_platform.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeDAO, Long> {

    List<RecipeDAO> findByAuthor(String author);
    boolean existsByEmail(String email);
}
