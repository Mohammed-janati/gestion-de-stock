package com.backend.services;

import com.backend.Entity.category;
import com.backend.dto.categoryDto;
import com.backend.repository.categoryRepo;
import com.backend.repository.productRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class categoryService {
    public categoryService(categoryRepo categoryRepository, productRepo productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    private final categoryRepo categoryRepository;
    

    private final productRepo productRepository;
    
    // Create a new category
    public categoryDto createCategory(categoryDto categoryDto) {
        // Check if category name already exists
        if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
        }
        
        category categorys = category.builder()
                .name(categoryDto.getName())

                .build();
        
        category savedCategory = categoryRepository.save(categorys);
        return convertToDto(savedCategory);
    }
    
    // Get all categories
    public List<categoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    

    
    // Get category by ID
    public categoryDto getCategoryById(Long id) {
        category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDto(category);
    }
    
    // Update category
    public categoryDto updateCategory(Long id, categoryDto categoryDto) {
        category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Check if category name is being changed and if it already exists
        if (!existingCategory.getName().equalsIgnoreCase(categoryDto.getName())) {
            if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
                throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
            }
        }
        
        existingCategory.setName(categoryDto.getName());

        category savedCategory = categoryRepository.save(existingCategory);
        return convertToDto(savedCategory);
    }
    
    // Delete category
    public void deleteCategory(Long id) {
        category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Check if category has products
        long productCount = productRepository.countByCategoryId(id);
        if (productCount > 0) {
            throw new RuntimeException("Cannot delete category. It has " + productCount + " products. Please remove or reassign products first.");
        }
        
        categoryRepository.deleteById(id);
    }
    
   
    
   
    
   
    
    // Convert entity to DTO
    public categoryDto convertToDto(category category) {
        // Count products in this category
        long productCount = productRepository.countByCategoryId(category.getId());
        
        return categoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                
                .productCount(productCount)
                .build();
    }
    
   
}
