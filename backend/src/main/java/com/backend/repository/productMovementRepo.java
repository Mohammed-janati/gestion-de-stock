package com.backend.repository;

import com.backend.Entity.productMovement;
import com.backend.Entity.product;
import com.backend.Entity.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface productMovementRepo extends JpaRepository<productMovement, Long> {

    // Find movements by product
    List<productMovement> findByProduct(product product);
    
    // Find movements by product with pagination
    Page<productMovement> findByProduct(product product, Pageable pageable);

    
    
   

    // Find movements by movement type
    List<productMovement> findByMovementType(productMovement.MovementType movementType);
    
    // Find movements by movement type with pagination
    Page<productMovement> findByMovementType(productMovement.MovementType movementType, Pageable pageable);

    // Find movements by date range
    List<productMovement> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find movements by date range with pagination
    Page<productMovement> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find movements by product and date range
    List<productMovement> findByProductAndMovementDateBetween(product product, LocalDateTime startDate, LocalDateTime endDate);

    // Find movements by product and movement type
    List<productMovement> findByProductAndMovementType(product product, productMovement.MovementType movementType);

    @Query("SELECT m.movementType, SUM(m.quantity) FROM productMovement m WHERE m.product = :product GROUP BY m.movementType")
    List<Object[]> getMovementSummaryByProduct(@Param("product") product product);



    // Custom query to get recent movements for a product
    @Query("SELECT m FROM productMovement m WHERE m.product = :product ORDER BY m.movementDate DESC")
    List<productMovement> getRecentMovementsByProduct(@Param("product") product product, Pageable pageable);

    // Custom query to get movements by date range and movement type
    @Query("SELECT m FROM productMovement m WHERE m.movementDate BETWEEN :startDate AND :endDate AND m.movementType = :movementType")
    List<productMovement> findByDateRangeAndMovementType(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("movementType") productMovement.MovementType movementType
    );



    Long getTotalStockInByProduct(product product);

    void deleteByProductId(Long id);
}
