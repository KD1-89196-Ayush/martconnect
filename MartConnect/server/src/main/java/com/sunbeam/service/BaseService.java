package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface providing common CRUD operations
 * @param <T> The entity type
 * @param <ID> The ID type of the entity
 */
public interface BaseService<T, ID> {
    
    /**
     * Save an entity
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);
    
    /**
     * Find an entity by ID
     * @param id The ID to search for
     * @return Optional containing the entity if found
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Delete an entity by ID
     * @param id The ID of the entity to delete
     */
    void deleteById(ID id);
    
    /**
     * Check if an entity exists by ID
     * @param id The ID to check
     * @return true if entity exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Count all entities
     * @return The total count of entities
     */
    long count();
} 