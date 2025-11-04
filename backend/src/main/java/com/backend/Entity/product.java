package com.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Column(nullable = false, length = 50, unique = true)
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String productCode;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    private double price;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Column(nullable = false)
    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minStockLevel;

    // Many-to-one relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<allert> alerts;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods for stock management
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }

    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    

    public boolean removeStock(int amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }
}
