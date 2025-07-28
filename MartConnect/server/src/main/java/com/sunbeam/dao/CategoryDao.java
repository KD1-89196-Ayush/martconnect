package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entities.Category;


public interface CategoryDao extends JpaRepository<Category, Long> {
		List<Category> findByStatusTrue();
		boolean existsByName(String categoryName);
}
