package com.challenge.backend.stockapi.repository;

import com.challenge.backend.stockapi.entity.Product;
import com.challenge.backend.stockapi.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT a FROM Product a WHERE a.code = ?1")
    Optional<Product> findProductByCode(int code);

    @Query("SELECT a FROM Product a WHERE a.type = ?1")
    List<Product> listProductsByType(ProductType type);
}
