package com.backend.repository;

import com.backend.Entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepo extends JpaRepository<user,Long> {
    user findByUsername(String username);
}
