package com.backend.repository;

import com.backend.Entity.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface productRepo extends JpaRepository<product, Long> {

    // Find products by category
    List<product> findByCategoryId(Long categoryId);

    // Find products by name (case insensitive)
    List<product> findByNameContainingIgnoreCase(String name);

    // Find products by product code
    Optional<product> findByProductCode(String productCode);

    // Find products with low stock
    @Query("SELECT p FROM product p WHERE p.quantity <= p.minStockLevel")
    List<product> findLowStockProducts();

    // Find out of stock products
    @Query("SELECT p FROM product p WHERE p.quantity <= 0")
    List<product> findOutOfStockProducts();

    // Find active products
    List<product> findByIsActiveTrue();

    // Find products by category and active status
    List<product> findByCategoryIdAndIsActiveTrue(Long categoryId);


    long countByCategoryId(Long id);
}