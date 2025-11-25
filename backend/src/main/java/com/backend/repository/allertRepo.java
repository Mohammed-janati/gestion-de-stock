package com.backend.repository;

import com.backend.Entity.allert;
import com.backend.Entity.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface allertRepo extends JpaRepository<allert, Integer> {


    Optional<allert> findByProductId(Long id);

    void deleteByProductId(Long id);
}
