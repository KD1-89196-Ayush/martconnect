package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.dto.CategoryDto;
import com.sunbeam.entities.Category;

public interface CategoryService extends BaseService<Category, Long> {
    
    // GET methods - return DTOs for frontend
    CategoryDto getCategoryById(Long categoryId);
    List<CategoryDto> getAllCategories();
    Optional<CategoryDto> findCategoryByName(String name);
    List<CategoryDto> findCategoriesByNameContainingIgnoreCase(String name);
    List<CategoryDto> findCategoriesByDescriptionContainingIgnoreCase(String description);
    List<CategoryDto> searchCategories(String searchTerm);
    
    // POST/PUT/DELETE methods - return ApiResponse
    ApiResponse createCategory(CategoryDto categoryDto);
    ApiResponse updateCategory(Long categoryId, CategoryDto categoryDto);
    ApiResponse deleteCategory(Long categoryId);
    
    // Business logic methods
    boolean existsByName(String name);
} 