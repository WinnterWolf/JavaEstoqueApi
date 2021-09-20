package com.challenge.backend.stockapi.repository;

import com.challenge.backend.stockapi.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

    @Query("SELECT a FROM StockTransaction a WHERE a.product = ?1")
    List<StockTransaction> findTransactionByProduct(int product);
}
