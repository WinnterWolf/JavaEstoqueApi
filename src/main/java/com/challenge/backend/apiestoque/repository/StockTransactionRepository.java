package com.challenge.backend.apiestoque.repository;

import com.challenge.backend.apiestoque.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
}
