package com.backend.controller;

import com.backend.Entity.productMovement;
import com.backend.services.productMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/product-movements")
@Tag(name = "Product Movement Management")
@RequiredArgsConstructor
public class productMovementController {

    private final productMovementService productMovementService;

    @Operation(summary = "Get all product movements", description = "Retrieves all product movements sorted by date (newest first)")
    @GetMapping
    public ResponseEntity<List<productMovement>> getAllMovements() {
        try {
            System.out.println("hi");
            List<productMovement> movements = productMovementService.getAllMovementsSortedByDate();
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all product movements with pagination", description = "Retrieves all product movements with pagination, sorted by date (newest first)")
    @GetMapping("/paginated")
    public ResponseEntity<Page<productMovement>> getAllMovementsPaginated(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        try {
            Page<productMovement> movements = productMovementService.getAllMovementsSortedByDate(page, size);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by product ID", description = "Retrieves all movements for a specific product, sorted by date")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<productMovement>> getMovementsByProductId(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByProductId(productId);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by product ID with pagination", description = "Retrieves movements for a specific product with pagination, sorted by date")
    @GetMapping("/product/{productId}/paginated")
    public ResponseEntity<Page<productMovement>> getMovementsByProductIdPaginated(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        try {
            Page<productMovement> movements = productMovementService.getMovementsByProductId(productId, page, size);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by movement type", description = "Retrieves all movements of a specific type, sorted by date")
    @GetMapping("/type/{movementType}")
    public ResponseEntity<List<productMovement>> getMovementsByType(
            @Parameter(description = "Movement type (IN or OUT)") @PathVariable productMovement.MovementType movementType) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByType(movementType);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by movement type with pagination", description = "Retrieves movements of a specific type with pagination, sorted by date")
    @GetMapping("/type/{movementType}/paginated")
    public ResponseEntity<Page<productMovement>> getMovementsByTypePaginated(
            @Parameter(description = "Movement type (IN or OUT)") @PathVariable productMovement.MovementType movementType,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        try {
            Page<productMovement> movements = productMovementService.getMovementsByType(movementType, page, size);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by cause", description = "Retrieves all movements with a specific cause, sorted by date")
    @GetMapping("/cause/{cause}")
    public ResponseEntity<List<productMovement>> getMovementsByCause(
            @Parameter(description = "Movement cause") @PathVariable productMovement.Cause cause) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByCause(cause);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by date range", description = "Retrieves movements within a specific date range, sorted by date")
    @GetMapping("/date-range")
    public ResponseEntity<List<productMovement>> getMovementsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByDateRange(startDate, endDate);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by date range with pagination", description = "Retrieves movements within a specific date range with pagination, sorted by date")
    @GetMapping("/date-range/paginated")
    public ResponseEntity<Page<productMovement>> getMovementsByDateRangePaginated(
            @Parameter(description = "Start date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        try {
            Page<productMovement> movements = productMovementService.getMovementsByDateRange(startDate, endDate, page, size);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movements by product and date range", description = "Retrieves movements for a specific product within a date range, sorted by date")
    @GetMapping("/product/{productId}/date-range")
    public ResponseEntity<List<productMovement>> getMovementsByProductAndDateRange(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Start date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByProductAndDateRange(productId, startDate, endDate);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get recent movements by product", description = "Retrieves recent movements for a specific product")
    @GetMapping("/product/{productId}/recent")
    public ResponseEntity<List<productMovement>> getRecentMovementsByProduct(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Number of recent movements to retrieve") @RequestParam(defaultValue = "10") int limit) {
        try {
            List<productMovement> movements = productMovementService.getRecentMovementsByProduct(productId, limit);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get movement summary by product", description = "Retrieves movement summary statistics for a specific product")
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<List<Object[]>> getMovementSummaryByProduct(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        try {
            List<Object[]> summary = productMovementService.getMovementSummaryByProduct(productId);
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Operation(summary = "Get movements by product and type", description = "Retrieves movements for a specific product and movement type")
    @GetMapping("/product/{productId}/type/{movementType}")
    public ResponseEntity<List<productMovement>> getMovementsByProductAndType(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Movement type (IN or OUT)") @PathVariable productMovement.MovementType movementType) {
        try {
            List<productMovement> movements = productMovementService.getMovementsByProductAndType(productId, movementType);
            return new ResponseEntity<>(movements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
