package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    
    /**
     * Find products by name
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by category
     */
    List<Product> findByCategory_NameContainingIgnoreCase(String categoryName);
    
    /**
     * Find products by seller
     */
    List<Product> findBySeller_SellerId(Integer sellerId);
    
    /**
     * Find products by name or description containing search term
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm);
    

} 