package com.backend.services;

import com.backend.Entity.productMovement;
import com.backend.Entity.product;
import com.backend.repository.productMovementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class productMovementService {

    private final productMovementRepo productMovementRepository;

    // Get all movements sorted by date (newest first)
    public List<productMovement> getAllMovementsSortedByDate() {
        return productMovementRepository.findAll(Sort.by(Sort.Direction.DESC, "movementDate"));
    }

    // Get all movements with pagination, sorted by date
    public Page<productMovement> getAllMovementsSortedByDate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.findAll(pageable);
    }

    // Get movements by product ID, sorted by date
    public List<productMovement> getMovementsByProductId(Long productId) {
        product product = new product();
        product.setId(productId);
        return productMovementRepository.findByProduct(product);
    }

    // Get movements by product ID with pagination, sorted by date
    public Page<productMovement> getMovementsByProductId(Long productId, int page, int size) {
        product product = new product();
        product.setId(productId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.findByProduct(product, pageable);
    }

    // Get movements by movement type, sorted by date
    public List<productMovement> getMovementsByType(productMovement.MovementType movementType) {
        return productMovementRepository.findByMovementType(movementType);
    }

    // Get movements by movement type with pagination, sorted by date
    public Page<productMovement> getMovementsByType(productMovement.MovementType movementType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.findByMovementType(movementType, pageable);
    }

    // Get movements by date range, sorted by date
    public List<productMovement> getMovementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return productMovementRepository.findByMovementDateBetween(startDate, endDate);
    }

    // Get movements by date range with pagination, sorted by date
    public Page<productMovement> getMovementsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.findByMovementDateBetween(startDate, endDate, pageable);
    }

    // Get movements by product and date range
    public List<productMovement> getMovementsByProductAndDateRange(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        product product = new product();
        product.setId(productId);
        return productMovementRepository.findByProductAndMovementDateBetween(product, startDate, endDate);
    }

    // Get movements by product and movement type
    public List<productMovement> getMovementsByProductAndType(Long productId, productMovement.MovementType movementType) {
        product product = new product();
        product.setId(productId);
        return productMovementRepository.findByProductAndMovementType(product, movementType);
    }

    // Get recent movements for a product
    public List<productMovement> getRecentMovementsByProduct(Long productId, int limit) {
        product product = new product();
        product.setId(productId);
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.getRecentMovementsByProduct(product, pageable);
    }

    // Get movement summary for a product
    public List<Object[]> getMovementSummaryByProduct(Long productId) {
        product product = new product();
        product.setId(productId);
        return productMovementRepository.getMovementSummaryByProduct(product);
    }



    // Get movements by cause
    public List<productMovement> getMovementsByCause(productMovement.Cause cause) {
        return productMovementRepository.findAll().stream()
                .filter(movement -> movement.getCause() == cause)
                .sorted((m1, m2) -> m2.getMovementDate().compareTo(m1.getMovementDate()))
                .toList();
    }

    // Get movements by cause with pagination
    public Page<productMovement> getMovementsByCause(productMovement.Cause cause, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
        return productMovementRepository.findAll(pageable).map(movement -> {
            if (movement.getCause() == cause) {
                return movement;
            }
            return null;
        }).map(movement -> movement);
    }
}
