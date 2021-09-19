package com.challenge.backend.stockapi.service;

import com.challenge.backend.stockapi.dto.MessageResponseDTO;
import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.entity.Product;
import com.challenge.backend.stockapi.entity.StockTransaction;
import com.challenge.backend.stockapi.enums.TransactionType;
import com.challenge.backend.stockapi.exceptions.ProductNotFoundException;
import com.challenge.backend.stockapi.mapper.ProductMapper;
import com.challenge.backend.stockapi.repository.ProductRepository;
import com.challenge.backend.stockapi.repository.StockTransactionRepository;
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

        return createMessageResponse(savedProduct.getId(), "Created Product with ID ");
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
        Product product = verifyIfProductExists(id);
        return productMapper.toDTO(product);
    }

    public void delete(Long id) throws ProductNotFoundException {
        Product product = verifyIfProductExists(id);

//        TODO verificar se existem transações com esse produto e exclui-las também.
        productRepository.deleteById(id);
    }


    public MessageResponseDTO updateProductById(Long id, ProductDTO productDTO) throws ProductNotFoundException {

        verifyIfProductExists(id);
        Product productToUpdate = productMapper.toModel(productDTO);
        Product updatedProduct = productRepository.save(productToUpdate);

        return createMessageResponse(updatedProduct.getId(), "Updated Product with ID ");
    }

    private MessageResponseDTO createMessageResponse(Long id, String s) {
        return MessageResponseDTO
                .builder()
                .message(s + id)
                .build();
    }

    private Product verifyIfProductExists(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
