package com.backend.repository;

import com.backend.Entity.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface categoryRepo extends JpaRepository<category, Long> {
    
    // Find category by name
    Optional<category> findByName(String name);
    
   
    
    // Check if category exists by name (case insensitive)
    boolean existsByNameIgnoreCase(String name);
}
