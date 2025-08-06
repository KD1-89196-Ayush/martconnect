package com.sunbeam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.CategoryDto;
import com.sunbeam.service.CategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /*
     * Desc - Get all categories
     * URL - http://host:port/categories
     * Method - GET
     * Payload - none
     * Successful Resp - SC 200, body - List<CategoryDto>
     * Empty list - SC 204, no body content
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        System.out.println("in get all categories");
        List<CategoryDto> categories = categoryService.getAllCategories();
        if (categories.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(categories);
    }
    
    /*
     * Desc - Get category by id
     * URL - http://host:port/categories/{categoryId}
     * Method - GET
     * Payload - none
     * Success Resp - SC 200 + CategoryDto
     * Error resp - SC 404, ApiResponse with error msg
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        System.out.println("in get category " + categoryId);
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }
    
    /*
     * Desc - Add new category
     * URL - http://host:port/categories
     * Method - POST
     * Payload - JSON representation of CategoryDto -> @RequestBody
     * Success Resp - SC 201 + ApiResponse with success msg
     * Error resp - SC 400, ApiResponse with error msg
     */
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto newCategory) {
        System.out.println("in add category " + newCategory);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(newCategory));
    }
    
    /*
     * Desc - Update category details by id
     * URL - http://host:port/categories/{categoryId}
     * Method - PUT
     * Payload - Updated CategoryDto
     * Success Resp - SC 200 + ApiResponse with success msg
     * Error resp - SC 404, ApiResponse with error msg
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        System.out.println("in update category " + categoryId);
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
    }
    
    /*
     * Desc - Delete category by id
     * URL - http://host:port/categories/{categoryId}
     * Method - DELETE
     * Payload - none
     * Success Resp - SC 200 + ApiResponse with success msg
     * Error resp - SC 404, ApiResponse with error msg
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        System.out.println("in delete category " + categoryId);
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }
    
    /*
     * Desc - Search categories by name
     * URL - http://host:port/categories/search?name={searchTerm}
     * Method - GET
     * Payload - none
     * Success Resp - SC 200 + List<CategoryDto>
     * Empty list - SC 204, no body content
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCategories(@RequestParam String name) {
        System.out.println("in search categories " + name);
        List<CategoryDto> categories = categoryService.searchCategories(name);
        if (categories.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(categories);
    }
    
    /*
     * Desc - Find category by name
     * URL - http://host:port/categories/name?name={name}
     * Method - GET
     * Payload - none
     * Success Resp - SC 200 + CategoryDto
     * Not found - SC 204, no body content
     */
    @GetMapping("/name")
    public ResponseEntity<?> findCategoryByName(@RequestParam String name) {
        System.out.println("in find category by name " + name);
        return categoryService.findCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
    
    /*
     * Desc - Find categories by description containing
     * URL - http://host:port/categories/description?description={description}
     * Method - GET
     * Payload - none
     * Success Resp - SC 200 + List<CategoryDto>
     * Empty list - SC 204, no body content
     */
    @GetMapping("/description")
    public ResponseEntity<?> findCategoriesByDescription(@RequestParam String description) {
        System.out.println("in find categories by description " + description);
        List<CategoryDto> categories = categoryService.findCategoriesByDescriptionContainingIgnoreCase(description);
        if (categories.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(categories);
    }
} 