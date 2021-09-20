package com.challenge.backend.stockapi.repository;

import com.challenge.backend.stockapi.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

    @Query("SELECT a FROM StockTransaction a WHERE a.product = ?1")
    List<StockTransaction> findTransactionsByProduct(int product);

    @Query("SELECT a FROM StockTransaction a WHERE a.product = ?1 AND a.transactionType=  com.challenge.backend.stockapi.enums.TransactionType.OUTCOME")
    List<StockTransaction> findTransactionsByProductOutcome(int code);
}
