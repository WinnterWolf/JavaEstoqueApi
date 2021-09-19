package com.challenge.backend.apiestoque.service;

import com.challenge.backend.apiestoque.dto.MessageResponseDTO;
import com.challenge.backend.apiestoque.dto.request.ProductDTO;
import com.challenge.backend.apiestoque.entity.Product;
import com.challenge.backend.apiestoque.entity.StockTransaction;
import com.challenge.backend.apiestoque.enums.TransactionType;
import com.challenge.backend.apiestoque.exceptions.ProductNotFoundException;
import com.challenge.backend.apiestoque.mapper.ProductMapper;
import com.challenge.backend.apiestoque.repository.ProductRepository;
import com.challenge.backend.apiestoque.repository.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    private ProductRepository productRepository;
    private StockTransactionRepository stockTransactionRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

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
    public MessageResponseDTO createProduct(ProductDTO productDTO) {


        Product productToSave = productMapper.toModel(productDTO);
        Product savedProduct = productRepository.save(productToSave);

        return MessageResponseDTO
                .builder()
                .message("Created Product with ID " + savedProduct.getId())
                .build();
    }

    //TODO
    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createTransaction(StockTransaction stockTransaction) {


        StockTransaction savedStockTransaction = stockTransactionRepository.save(stockTransaction);
        TransactionType transactionType = stockTransaction.getTransactionType();
        return MessageResponseDTO
                .builder()
                .message("Created " + transactionType + " Transaction with ID " + savedStockTransaction.getId())
                .build();
    }

    public List<ProductDTO> listAll() {
        List<Product> allProducts =  productRepository.findAll();
        return allProducts.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }
}
