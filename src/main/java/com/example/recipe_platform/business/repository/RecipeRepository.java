package com.example.recipe_platform.business.repository;

import com.example.recipe_platform.business.repository.model.RecipeDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeDAO, Long> {

    List<RecipeDAO> findByAuthor(String author);
}
