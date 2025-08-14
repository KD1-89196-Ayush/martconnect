package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CategoryDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.dto.CategoryDto;
import com.sunbeam.entities.Category;
import com.sunbeam.entities.Product;
import com.sunbeam.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Core: Add category with validation
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        
        if (categoryDao.existsByName(category.getName())) {
            throw new RuntimeException("Category with name " + category.getName() + " already exists");
        }
        
        category.setCreatedAt(LocalDateTime.now());
        Category savedCategory = categoryDao.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }
    
    // Core: Update category
    @Override
    public CategoryDto updateCategory(Integer categoryId, CategoryDto categoryDto) {
        Optional<Category> existingCategory = categoryDao.findById(categoryId);
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        
        Category existing = existingCategory.get();
        Category category = modelMapper.map(categoryDto, Category.class);
        
        if (category.getName() != null) existing.setName(category.getName());
        if (category.getDescription() != null) existing.setDescription(category.getDescription());
        
        Category savedCategory = categoryDao.save(existing);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }
    
    // Essential CRUD methods
    @Override
    public CategoryDto save(CategoryDto categoryDto) { 
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryDao.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }
    
    @Override
    public Optional<CategoryDto> findById(Integer id) { 
        Optional<Category> categoryOpt = categoryDao.findById(id);
        return categoryOpt.map(category -> modelMapper.map(category, CategoryDto.class));
    }
    
    @Override
    public List<CategoryDto> findAll() { 
        return categoryDao.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Integer id) { categoryDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return categoryDao.existsById(id); }
    
    @Override
    public long count() { return categoryDao.count(); }
    
    // Essential search methods
    @Override
    public Optional<CategoryDto> findByName(String name) { 
        Optional<Category> categoryOpt = categoryDao.findByName(name);
        return categoryOpt.map(category -> modelMapper.map(category, CategoryDto.class));
    }
    
    @Override
    public boolean existsByName(String name) { return categoryDao.existsByName(name); }
    
//    @Override
//    public long countByNameContaining(String name) { 
//        return 0; 
//    }
//    
    @Override
    public void deleteCategory(Integer categoryId) {
        Optional<Category> categoryOpt = categoryDao.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        
        Category category = categoryOpt.get();
        List<Product> products = productDao.findByCategory_NameContainingIgnoreCase(category.getName());
        if (!products.isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing products. Please reassign or delete products first.");
        }
        
        categoryDao.deleteById(categoryId);
    }
    
    @Override
    public boolean validateCategoryName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }
    
//  @Override
//  public List<CategoryDto> findByNameContaining(String name) { 
//      // Since findByNameContainingIgnoreCase was removed, return empty list
//      return List.of();
//  }
}
