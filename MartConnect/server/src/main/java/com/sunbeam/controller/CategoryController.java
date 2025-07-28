package com.sunbeam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Category;
import com.sunbeam.service.CategoryService;


@RestController 
@RequestMapping("/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ResponseEntity<?> getAllAvailableCategories() {
		System.out.println("in get all");
		List<Category> categories = categoryService.getAvailableCategories();
		if (categories.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		return ResponseEntity.ok(categories);
	}
	
	@PostMapping
	public ResponseEntity<?> addCategory(@RequestBody 
			Category newCategory) {
		System.out.println("in add " + newCategory);
		try {
			return ResponseEntity
					.status(HttpStatus.CREATED)//SC 201
					.body(categoryService.addNewCategory(newCategory));
		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST) //SC 400
					.body(new ApiResponse(e.getMessage()));
		}
	}
}
