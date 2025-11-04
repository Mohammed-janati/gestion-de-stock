package com.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class allert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    @JsonIgnore
    private product product;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}
