package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

import com.sunbeam.dto.CategoryDto;

/**
 * Service interface for Category entity operations
 */
public interface CategoryService extends BaseService<CategoryDto, Integer> {
    
    Optional<CategoryDto> findByName(String name);
    
    boolean existsByName(String name);
    
    CategoryDto addCategory(CategoryDto categoryDto);
    
    CategoryDto updateCategory(Integer categoryId, CategoryDto categoryDto);
    
    void deleteCategory(Integer categoryId);
    
    boolean validateCategoryName(String name);
  
}
