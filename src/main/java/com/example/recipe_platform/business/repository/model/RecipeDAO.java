package com.example.recipe_platform.business.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class RecipeDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String ingredients;
    @Column(nullable = false)
    private String instruction;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false, unique = true)
    private String email;
}
