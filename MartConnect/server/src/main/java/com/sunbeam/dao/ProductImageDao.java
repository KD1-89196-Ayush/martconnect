package com.sunbeam.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.ProductImage;

@Repository
public interface ProductImageDao extends JpaRepository<ProductImage, Integer> {
    // Only basic JPA methods are needed since this DAO is not used by the frontend
} 