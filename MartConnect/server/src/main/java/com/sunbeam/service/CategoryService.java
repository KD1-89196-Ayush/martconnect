package com.sunbeam.service;

import java.util.List;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Category;

public interface CategoryService {
	List<Category> getAvailableCategories();
    ApiResponse addNewCategory(Category transientCategory);
}
