package com.sunbeam.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CategoryDao;
import com.sunbeam.dto.ApiResponse;
import com.sunbeam.dto.CategoryDto;
import com.sunbeam.entities.Category;
import com.sunbeam.exception.ResourceNotFoundException;
import com.sunbeam.service.CategoryService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;
    
    // GET methods - return DTOs for frontend
    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }
    
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryDao.findAll();
        return categories.stream()
                .map(entity -> modelMapper.map(entity, CategoryDto.class))
                .toList();
    }
    
    @Override
    public Optional<CategoryDto> findCategoryByName(String name) {
        return categoryDao.findByName(name)
                .map(entity -> modelMapper.map(entity, CategoryDto.class));
    }
    
    @Override
    public List<CategoryDto> findCategoriesByNameContainingIgnoreCase(String name) {
        List<Category> categories = categoryDao.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(entity -> modelMapper.map(entity, CategoryDto.class))
                .toList();
    }
    
    @Override
    public List<CategoryDto> findCategoriesByDescriptionContainingIgnoreCase(String description) {
        List<Category> categories = categoryDao.findByDescriptionContainingIgnoreCase(description);
        return categories.stream()
                .map(entity -> modelMapper.map(entity, CategoryDto.class))
                .toList();
    }
    
    // Business logic methods
    @Override
    public boolean existsByName(String name) {
        return categoryDao.existsByName(name);
    }
    
    // POST/PUT/DELETE methods - return ApiResponse
    @Override
    public ApiResponse deleteCategory(Long categoryId) {
        if (!categoryDao.existsById(categoryId)) {
            return new ApiResponse("Category not found with ID: " + categoryId);
        }
        categoryDao.deleteById(categoryId);
        return new ApiResponse("Category deleted successfully");
    }
    
    @Override
    public ApiResponse createCategory(CategoryDto categoryDto) {
        if (categoryDao.existsByName(categoryDto.getName())) {
            return new ApiResponse("Category with name " + categoryDto.getName() + " already exists");
        }
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryDao.save(category);
        return new ApiResponse("Category created successfully with ID " + savedCategory.getId());
    }
    
    @Override
    public ApiResponse updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        modelMapper.map(categoryDto, category);
        categoryDao.save(category);
        return new ApiResponse("Category updated successfully");
    }
    
    @Override
    public List<CategoryDto> searchCategories(String searchTerm) {
        List<Category> categories = categoryDao.findByNameContainingIgnoreCase(searchTerm);
        return categories.stream()
                .map(entity -> modelMapper.map(entity, CategoryDto.class))
                .toList();
    }
    
    // Basic CRUD operations from BaseService
    @Override
    public Category save(Category entity) {
        return categoryDao.save(entity);
    }
    
    @Override
    public Optional<Category> findById(Long id) {
        return categoryDao.findById(id);
    }
    
    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return categoryDao.existsById(id);
    }
    
    @Override
    public long count() {
        return categoryDao.count();
    }
} 