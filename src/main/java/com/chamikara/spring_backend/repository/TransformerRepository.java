package com.chamikara.spring_backend.repository;

import com.chamikara.spring_backend.entity.Transformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransformerRepository extends JpaRepository<Transformer, Long> {
    
    Optional<Transformer> findByNumber(String number);
    
    boolean existsByNumber(String number);
}
