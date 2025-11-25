package com.backend;

import com.backend.Entity.product;
import com.backend.Entity.category;
import com.backend.Entity.productMovement;
import com.backend.dto.productDto;
import com.backend.repository.allertRepo;
import com.backend.repository.productMovementRepo;
import com.backend.repository.productRepo;
import com.backend.repository.categoryRepo;
import com.backend.services.productService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class productServiceTest {
    @Mock
    private productRepo productRepository;
    @Mock
    private allertRepo allertRepo;
    @Mock
    private productMovementRepo productmovementRepository;
    @Mock
    private categoryRepo categoryRepository;
    @InjectMocks
    private productService service;

    product prod;
    productMovement prodmovement;
    productDto prodDto;
    category cat;

    @BeforeEach
    public void setUp() {
        cat = category.builder().id(1L).name("cat1").build();
        prod = product.builder()
                .id(1L)
                .name("prod1")
                .description("desc")
                .productCode("P001")
                .price(100L)
                .quantity(10)
                .minStockLevel(2)
                .category(cat)
                .isActive(true)
                .build();
        prodDto = productDto.builder()
                .id(1L)
                .name("prod1")
                .description("desc")
                .productCode("P001")
                .price(100L)
                .quantity(10)
                .minStockLevel(2)
                .categoryId(1L)
                .isActive(true)
                .build();
        prodmovement=productMovement.builder()
                .id(1L)
                .product(prod)
                .cause(productMovement.Cause.adjustement)
                .movementType(productMovement.MovementType.IN)
                .movementDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void testCreateProduct_Success() {
        when(productRepository.findByProductCode(prodDto.getProductCode())).thenReturn(Optional.empty());
        when(categoryRepository.findById(prodDto.getCategoryId())).thenReturn(Optional.of(cat));
        when(allertRepo.findByProductId(prodDto.getCategoryId())).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(prod);
        when(productmovementRepository.save(any())).thenReturn(prodmovement);
        productDto result = service.createProduct(prodDto);
        assertEquals(prodDto.getName(), result.getName());
    }

    @Test
    public void testCreateProduct_DuplicateCode() {
        when(productRepository.findByProductCode(prodDto.getProductCode())).thenReturn(Optional.of(prod));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(prodDto));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testCreateProduct_CategoryNotFound() {
        when(productRepository.findByProductCode(prodDto.getProductCode())).thenReturn(Optional.empty());
        when(categoryRepository.findById(prodDto.getCategoryId())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(prodDto));
        assertTrue(ex.getMessage().contains("Category not found"));
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(prod));
        List<productDto> result = service.getAllProducts();
        assertEquals(1, result.size());
        assertEquals(prod.getName(), result.get(0).getName());
    }

    @Test
    public void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        productDto result = service.getProductById(1L);
        assertEquals(prod.getName(), result.getName());
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getProductById(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testUpdateProduct_Success() {
        productDto updateDto = productDto.builder()
                .id(1L)
                .name("prod2")
                .description("desc2")
                .productCode("P002")
                .price(200.0)
                .quantity(20)
                .minStockLevel(3)
                .categoryId(1L)
                .isActive(false)
                .build();
        product updatedProd = product.builder()
                .id(1L)
                .name("prod2")
                .description("desc2")
                .productCode("P002")
                .price(200.0)
                .quantity(20)
                .minStockLevel(3)
                .category(cat)
                .isActive(false)
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        when(productRepository.findByProductCode(updateDto.getProductCode())).thenReturn(Optional.empty());
        when(allertRepo.findByProductId(any())).thenReturn(Optional.empty());
        when(categoryRepository.findById(updateDto.getCategoryId())).thenReturn(Optional.of(cat));
        when(productRepository.save(any())).thenReturn(updatedProd);
        productDto result = service.updateProduct(1L, updateDto);
        assertEquals("prod2", result.getName());
        assertEquals("P002", result.getProductCode());
    }

    @Test
    public void testUpdateProduct_DuplicateCode() {
        productDto updateDto = productDto.builder()
                .id(1L)
                .name("prod2")
                .description("desc2")
                .productCode("P002")
                .price(200.0)
                .quantity(20)
                .minStockLevel(3)
                .categoryId(1L)
                .isActive(false)
                .build();
        product otherProd = product.builder().id(2L).productCode("P002").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        when(productRepository.findByProductCode(updateDto.getProductCode())).thenReturn(Optional.of(otherProd));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateProduct(1L, updateDto));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        productDto updateDto = productDto.builder().id(1L).name("prod2").productCode("P002").build();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateProduct(1L, updateDto));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testUpdateProduct_CategoryNotFound() {
        productDto updateDto = productDto.builder().id(1L).name("prod2").productCode("P002").categoryId(2L).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        when(productRepository.findByProductCode(updateDto.getProductCode())).thenReturn(Optional.empty());
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateProduct(1L, updateDto));
        assertTrue(ex.getMessage().contains("Category not found"));
    }



    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteProduct(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testGetProductsByCategory() {
        when(productRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(prod));
        List<productDto> result = service.getProductsByCategory(1L);
        assertEquals(1, result.size());
        assertEquals(prod.getName(), result.get(0).getName());
    }

    @Test
    public void testGetLowStockProducts() {
        when(productRepository.findLowStockProducts()).thenReturn(Arrays.asList(prod));
        List<productDto> result = service.getLowStockProducts();
        assertEquals(1, result.size());
        assertEquals(prod.getName(), result.get(0).getName());
    }

    @Test
    public void testGetOutOfStockProducts() {
        when(productRepository.findOutOfStockProducts()).thenReturn(Arrays.asList(prod));
        List<productDto> result = service.getOutOfStockProducts();
        assertEquals(1, result.size());
        assertEquals(prod.getName(), result.get(0).getName());
    }

    @Test
    public void testUpdateStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        when(allertRepo.findByProductId(1L)).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(prod);
        productDto result = service.updateStock(1L, 5, productMovement.Cause.adjustement);
        assertEquals(5, result.getQuantity());
    }

    @Test
    public void testUpdateStock_NegativeQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateStock(1L, -1, productMovement.Cause.adjustement));
        assertTrue(ex.getMessage().contains("cannot be negative"));
    }

    @Test
    public void testUpdateStock_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateStock(1L, 5, productMovement.Cause.adjustement));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testRemoveStock_Success() {
        product prodWithStock = spy(prod);
        when(productRepository.findById(1L)).thenReturn(Optional.of(prodWithStock));
        when(allertRepo.findByProductId(1L)).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(prodWithStock);
        doReturn(true).when(prodWithStock).removeStock(5);
        productDto result = service.removeStock(1L, 5, productMovement.Cause.adjustement);
        assertEquals(prodWithStock.getName(), result.getName());
    }

    @Test
    public void testRemoveStock_InsufficientStock() {
        product prodWithStock = spy(prod);
        when(productRepository.findById(1L)).thenReturn(Optional.of(prodWithStock));
        doReturn(false).when(prodWithStock).removeStock(20);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.removeStock(1L, 20, productMovement.Cause.adjustement));
        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }

    @Test
    public void testRemoveStock_NegativeOrZeroAmount() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        RuntimeException ex1 = assertThrows(RuntimeException.class, () -> service.removeStock(1L, 0, productMovement.Cause.adjustement));
        assertTrue(ex1.getMessage().contains("must be positive"));
        RuntimeException ex2 = assertThrows(RuntimeException.class, () -> service.removeStock(1L, -5, productMovement.Cause.adjustement));
        assertTrue(ex2.getMessage().contains("must be positive"));
    }

    @Test
    public void testRemoveStock_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.removeStock(1L, 5, productMovement.Cause.adjustement));
        assertTrue(ex.getMessage().contains("not found"));
    }
}

