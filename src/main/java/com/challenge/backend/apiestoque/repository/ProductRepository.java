package com.challenge.backend.apiestoque.repository;

import com.challenge.backend.apiestoque.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
