package com.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class categoryDto {
    
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    @NotNull
    private String name;
    
   
    
    private Long productCount; // Number of products in this category
}
