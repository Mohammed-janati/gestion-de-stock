package com.backend.dto;

import com.backend.Entity.productMovement;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class productDto {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String productCode;
    
    @NotNull(message = "Price is required")
    private double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minStockLevel;
    
    private Long categoryId;
    private productMovement.Cause  cause;

    private Boolean isActive;
}
