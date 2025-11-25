package com.backend.services;

import com.backend.Entity.allert;
import com.backend.Entity.product;
import com.backend.Entity.category;
import com.backend.Entity.productMovement;

import com.backend.dto.productDto;
import com.backend.repository.allertRepo;
import com.backend.repository.productRepo;
import com.backend.repository.categoryRepo;
import com.backend.repository.productMovementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class productService {

    public productService(productRepo productRepository, allertRepo allertRepository, productMovementRepo productmovementrepo, categoryRepo categoryRepository) {
        this.productRepository = productRepository;
        this.allertRepository = allertRepository;
        this.productmovementrepo = productmovementrepo;
        this.categoryRepository = categoryRepository;
    }

    private final productRepo productRepository;
    private final allertRepo allertRepository;
    private final productMovementRepo productmovementrepo;


    private final categoryRepo categoryRepository;
    
    // Create a new product
    public productDto createProduct(productDto productDto) {
        // Check if product code already exists
        if (productRepository.findByProductCode(productDto.getProductCode()).isPresent()) {
            throw new RuntimeException("Product with code " + productDto.getProductCode() + " already exists");
        }
        
        // Validate category exists
        category category = null;
        if (productDto.getCategoryId() != null) {
            category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
        }
        
        product products = product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .productCode(productDto.getProductCode())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .minStockLevel(productDto.getMinStockLevel())
                .category(category)
                .isActive(productDto.getIsActive() != null ? productDto.getIsActive() : true)
                .build();
        
        product savedProduct = productRepository.save(products);

//mouvement registry
        productMovement movement = productMovement.builder()
                .product(savedProduct)
                .movementType(productMovement.MovementType.IN)
                .cause(productMovement.Cause.add)
                .quantity(savedProduct.getQuantity())
                .build();
        productmovementrepo.save(movement);

//check for alert

            allert(savedProduct);

        return convertToDto(savedProduct);
    }
    
    // Get all products
    public List<productDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get product by ID
    public productDto getProductById(Long id) {
        product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }
    
    // Update product
    public productDto updateProduct(Long id, productDto productDto) {
        product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Check if product code is being changed and if it already exists
        if (!existingProduct.getProductCode().equals(productDto.getProductCode())) {
            if (productRepository.findByProductCode(productDto.getProductCode()).isPresent()) {
                throw new RuntimeException("Product with code " + productDto.getProductCode() + " already exists");
            }
        }
        
        // Validate category exists
        category category = null;
        if (productDto.getCategoryId() != null) {
            category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
        }
        
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setMinStockLevel(productDto.getMinStockLevel());
        existingProduct.setCategory(category);
        if (productDto.getIsActive() != null) {
            existingProduct.setIsActive(productDto.getIsActive());
        }
        
        product savedProduct = productRepository.save(existingProduct);

        //check for alert
allert(savedProduct);

        return convertToDto(savedProduct);
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.findById(id).orElseThrow(()->new RuntimeException("prod not found"));

        allertRepository.deleteByProductId(id);
        productmovementrepo.deleteByProductId(id);
        productRepository.deleteById(id);

    }
    
    // Get products by category
    public List<productDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    
    
    // Get low stock products
    public List<productDto> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get out of stock products
    public List<productDto> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Update stock quantity
    public productDto updateStock(Long id, Integer quantity,productMovement.Cause cause) {
        product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
        var oldQuantity=product.getQuantity();
        product.setQuantity(quantity);
        product savedProduct = productRepository.save(product);
        productMovement movement;
        if(oldQuantity < quantity){
            movement = productMovement.builder()
                    .product(savedProduct)
                    .movementType(productMovement.MovementType.IN)
                    .cause(cause)
                    .quantity(quantity-oldQuantity )
                    .build();
        }else {
            movement = productMovement.builder()
                    .product(savedProduct)
                    .movementType(productMovement.MovementType.OUT)
                    .cause(cause)
                    .quantity( oldQuantity-quantity )
                    .build();
        }
        productmovementrepo.save(movement);
//check for alert
        allert(savedProduct);
        return convertToDto(savedProduct);
    }
    
   
    
    // Remove stock
    public productDto removeStock(Long id, Integer amount,productMovement.Cause cause) {
        product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        if (amount <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (!product.removeStock(amount)) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity());
        }
        var oldQuantity=product.getQuantity();
        product savedProduct = productRepository.save(product);

        productMovement movement = productMovement.builder()
                .product(savedProduct)
                .movementType(productMovement.MovementType.OUT)
                .cause(cause)
                .quantity(amount - oldQuantity)
                .build();
        productmovementrepo.save(movement);
 //check  for alert
        allert(savedProduct);
        return convertToDto(savedProduct);
    }
    
    // Convert entity to DTO
    private productDto convertToDto(product product) {
        return productDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .productCode(product.getProductCode())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .minStockLevel(product.getMinStockLevel())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .isActive(product.getIsActive())
                .build();
    }

    public void allert(product d) {
        if (d.getQuantity() > d.getMinStockLevel()) {

            allertRepository.findByProductId(d.getId()).ifPresent(allertRepository::delete);
            return;
        }

        if (d.getQuantity() < d.getMinStockLevel()) {
            var x = allert.builder()
                    .product(d)
                    .message("product " + d.getName() + " is low in stock")
                    .build();
            allertRepository.save(x);
        }
    }
}
