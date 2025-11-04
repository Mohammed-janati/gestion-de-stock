package com.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_movements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class productMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    @JsonIgnore
    private product product;

    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Movement type is required")
    private Cause cause;

    @Column(name = "movement_date", nullable = false)
    @Builder.Default
    private LocalDateTime movementDate = LocalDateTime.now();

    



    // Enum for movement types
    public enum MovementType {
        IN,
        OUT
    }
    public enum Cause {
        sold,
        adjustement,
        expired,
        add,
        banned
    }
}
