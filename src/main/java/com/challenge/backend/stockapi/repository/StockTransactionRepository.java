package com.challenge.backend.stockapi.repository;

import com.challenge.backend.stockapi.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
}
