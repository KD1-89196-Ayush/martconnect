package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entities.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {
    
    //find category by name
    Optional<Category> findByName(String name);
    
    //find categories by name containing
    List<Category> findByNameContainingIgnoreCase(String name);
    
    //find categories by description containing
    List<Category> findByDescriptionContainingIgnoreCase(String description);
    
    //exists by name
    boolean existsByName(String name);
} 