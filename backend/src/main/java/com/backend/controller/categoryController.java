package com.backend.controller;

import com.backend.dto.categoryDto;
import com.backend.services.categoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "category")

public class categoryController {

    public categoryController(com.backend.services.categoryService categoryService) {
        this.categoryService = categoryService;
    }

    private final categoryService categoryService;
    
    @Operation(summary = "Create category", description = "Create a new category")
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody categoryDto categoryDto) {
        try {
            categoryDto createdCategory = categoryService.createCategory(categoryDto);
            return new ResponseEntity<>("Category created ", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all categories", description = "Get all categories")
    @GetMapping
    public ResponseEntity<List<categoryDto>> getAllCategories() {
        List<categoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }



    @Operation(summary = "Get category by ID", description = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<categoryDto> getCategoryById(@PathVariable Long id) {
        try {
            categoryDto category = categoryService.getCategoryById(id);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException("category not found");
        }
    }

    @Operation(summary = "Update category", description = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody categoryDto categoryDto) {
        try {
            categoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
            return new ResponseEntity<>("Category updated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete category", description = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
  
    
    
    
   
}
