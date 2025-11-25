package com.backend.controller;

import com.backend.Entity.productMovement;
import com.backend.dto.productDto;
import com.backend.services.productService;
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
@RequestMapping("/api/products")
@Tag(name = "product management")
public class productController {
    public productController(com.backend.services.productService productService) {
        this.productService = productService;
    }

    private final productService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product in the system")
    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody productDto productDto) {
        try {
            productDto createdProduct = productService.createProduct(productDto);
            return new ResponseEntity<>("Product created", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Get all products", description = "Retrieves all products")
    @GetMapping
    public ResponseEntity<List<productDto>> getAllProducts() {
        List<productDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<productDto> getProductById(@PathVariable Long id) {
        try {
            productDto product = productService.getProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException("Product not found");
        }
    }
    
    @Operation(summary = "Update product", description = "Updates an existing product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody productDto productDto) {
        try {
            productDto updatedProduct = productService.updateProduct(id, productDto);
            return new ResponseEntity<>("Product updated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Delete product", description = "Deletes a product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get products by category", description = "Retrieves products by category ID")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<productDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<productDto> products = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
   
    @Operation(summary = "Get low stock products", description = "Retrieves products with low stock levels")
    @GetMapping("/low-stock")
    public ResponseEntity<List<productDto>> getLowStockProducts() {
        List<productDto> products = productService.getLowStockProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @Operation(summary = "Get out of stock products", description = "Retrieves products that are out of stock")
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<productDto>> getOutOfStockProducts() {
        List<productDto> products = productService.getOutOfStockProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @Operation(summary = "Update stock quantity", description = "Updates the stock quantity for a product")
    @PutMapping("/{id}/stock")
    public ResponseEntity<String> updateStock(@PathVariable Long id, @RequestParam Integer quantity, @RequestBody productMovement.Cause cause) {
        try {
            productDto updatedProduct = productService.updateStock(id, quantity,cause);
            return new ResponseEntity<>("stock updated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove stock", description = "Removes a specified amount from product stock")
    @PostMapping("/{id}/remove-stock")
    public ResponseEntity<String> removeStock(@PathVariable Long id, @RequestParam Integer amount, @RequestBody productMovement.Cause cause) {
        try {
            productDto updatedProduct = productService.removeStock(id, amount,cause);
            return new ResponseEntity<>("stock updated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Get product statistics", description = "Retrieves statistics about products")
    @GetMapping("/stats")
    public ResponseEntity<ProductStats> getProductStats() {
        try {
            List<productDto> allProducts = productService.getAllProducts();
            List<productDto> lowStockProducts = productService.getLowStockProducts();
            List<productDto> outOfStockProducts = productService.getOutOfStockProducts();
            
            int totalProducts = allProducts.size();
            int lowStockCount = lowStockProducts.size();
            int outOfStockCount = outOfStockProducts.size();
            int activeProducts = (int) allProducts.stream().filter(p -> p.getIsActive()).count();
            
            return new ResponseEntity<>(new ProductStats(totalProducts, activeProducts, lowStockCount, outOfStockCount), HttpStatus.OK);
        } catch (Exception e) {
throw new RuntimeException(" error fetching stat");
        }
    }
    
    // Inner class for statistics
    public static class ProductStats {
        private int totalProducts;
        private int activeProducts;
        private int lowStockProducts;
        private int outOfStockProducts;
        
        public ProductStats(int totalProducts, int activeProducts, int lowStockProducts, int outOfStockProducts) {
            this.totalProducts = totalProducts;
            this.activeProducts = activeProducts;
            this.lowStockProducts = lowStockProducts;
            this.outOfStockProducts = outOfStockProducts;
        }
        
        // Getters
        public int getTotalProducts() { return totalProducts; }
        public int getActiveProducts() { return activeProducts; }
        public int getLowStockProducts() { return lowStockProducts; }
        public int getOutOfStockProducts() { return outOfStockProducts; }
    }
}
