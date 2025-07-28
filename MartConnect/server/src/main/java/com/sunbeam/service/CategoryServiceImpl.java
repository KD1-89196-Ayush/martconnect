package com.sunbeam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.custom_exceptions.InvalidInputException;
import com.sunbeam.dao.CategoryDao;
import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Category;


@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryDao categoryDao;

    CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
	@Override
	public List<Category> getAvailableCategories() {
		// TODO Auto-generated method stub
		return categoryDao.findByStatusTrue();
	}
	@Override
	public ApiResponse addNewCategory(Category transientCategory) {
				if(categoryDao.existsByName(transientCategory.getName()))
					throw new InvalidInputException("Duplicate category name!!!!!!!!!!");
				transientCategory.setStatus(true);
				Category persistentEntity = categoryDao.save
						(transientCategory);
				return new ApiResponse(
						"Added new restaurant with ID "
						+persistentEntity.getId());
	}
}
