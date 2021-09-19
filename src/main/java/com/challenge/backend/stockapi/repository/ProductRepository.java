package com.challenge.backend.stockapi.repository;

import com.challenge.backend.stockapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
