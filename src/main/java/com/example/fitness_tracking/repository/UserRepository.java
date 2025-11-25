package com.example.fitness_tracking.repository;

import com.example.fitness_tracking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    List<User> findByFitnessLevel(User.FitnessLevel fitnessLevel);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);
    
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    long countByFitnessLevel(User.FitnessLevel fitnessLevel);
}