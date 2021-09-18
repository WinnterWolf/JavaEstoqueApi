package com.challenge.backend.apiestoque.service;

import com.challenge.backend.apiestoque.dto.MessageResponseDTO;
import com.challenge.backend.apiestoque.entity.Product;
import com.challenge.backend.apiestoque.entity.StockTransaction;
import com.challenge.backend.apiestoque.enums.TransactionType;
import com.challenge.backend.apiestoque.repository.ProductRepository;
import com.challenge.backend.apiestoque.repository.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class StockService {

    private ProductRepository productRepository;
    private StockTransactionRepository stockTransactionRepository;

    @Autowired
    public void ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public StockService(StockTransactionRepository stockTransactionRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
    }

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createProduct(Product product) {

        Product savedProduct = productRepository.save(product);
        return MessageResponseDTO
                .builder()
                .message("Created Product with code: " + savedProduct.getCode())
                .build();
    }

    //Em desenvolvimento
    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createTransaction(StockTransaction stockTransaction) {


        StockTransaction savedStockTransaction = stockTransactionRepository.save(stockTransaction);
        TransactionType transactionType = stockTransaction.getTransactionType();
        return MessageResponseDTO
                .builder()
                .message("Created " + transactionType + " Transaction with ID: " + savedStockTransaction.getId())
                .build();
    }

}
