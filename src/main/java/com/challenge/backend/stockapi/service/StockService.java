package com.challenge.backend.stockapi.service;

import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.dto.request.StockTransactionDTO;
import com.challenge.backend.stockapi.dto.response.MessageResponseDTO;
import com.challenge.backend.stockapi.entity.Product;
import com.challenge.backend.stockapi.entity.ProductProfit;
import com.challenge.backend.stockapi.entity.ProductQuantity;
import com.challenge.backend.stockapi.entity.StockTransaction;
import com.challenge.backend.stockapi.enums.ProductType;
import com.challenge.backend.stockapi.enums.TransactionType;
import com.challenge.backend.stockapi.exceptions.NotEnoughInventoryException;
import com.challenge.backend.stockapi.exceptions.ProductNotFoundException;
import com.challenge.backend.stockapi.exceptions.TransactionNotFoundException;
import com.challenge.backend.stockapi.mapper.ProductMapper;
import com.challenge.backend.stockapi.mapper.StockTransactionMapper;
import com.challenge.backend.stockapi.repository.ProductRepository;
import com.challenge.backend.stockapi.repository.StockTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockService {

    private ProductRepository productRepository;
    private StockTransactionRepository stockTransactionRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final StockTransactionMapper stockTransactionMapper = StockTransactionMapper.INSTANCE;

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createProduct(ProductDTO productDTO) {


        Product productToSave = productMapper.toModel(productDTO);
        Product savedProduct = productRepository.save(productToSave);

        return createMessageResponse(savedProduct.getId(), "Created Product with ID ");
    }


    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createTransaction(StockTransactionDTO stockTransactionDTO) throws ProductNotFoundException, NotEnoughInventoryException {

        Product product = productRepository.findProductByCode(stockTransactionDTO.getProduct())
                .orElseThrow(() -> new ProductNotFoundException(" Product not found with code ", (long) stockTransactionDTO.getProduct()));
        validateTransaction(stockTransactionDTO, product);

        stockTransactionDTO.setTransactionDate(LocalDateTime.now().withNano(0));
        StockTransaction stockTransactionToSave = stockTransactionMapper.toModel(stockTransactionDTO);
        StockTransaction savedStockTransaction = stockTransactionRepository.save(stockTransactionToSave);

        return createMessageResponse(savedStockTransaction.getId(), "Created Transaction with ID ");
    }

    public List<ProductDTO> listAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductQuantity> listAllProductsByType(ProductType type) {
        List<Product> typedList = productRepository.listProductsByType(type);
        List<ProductQuantity> productQuantityList = new ArrayList<>();
        for (Product p: typedList) {
           ProductQuantity productQuantity = new ProductQuantity();
           productQuantity.setProduct(p);
           int stockOutcome = 0;
           List<StockTransaction> stockTransactions = stockTransactionRepository.findTransactionsByProductOutcome(p.getCode());
            for (StockTransaction transactions: stockTransactions) {
                stockOutcome+=transactions.getAmount();
            }
           productQuantity.setStockOutcome(stockOutcome);
            productQuantityList.add(productQuantity);
        }

        return productQuantityList;
    }

    public List<StockTransactionDTO> listAllTransactions() {
        List<StockTransaction> allTransactions = stockTransactionRepository.findAll();
        return allTransactions.stream()
                .map(stockTransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findProductById(long id) throws ProductNotFoundException {
        Product product = verifyIfProductExistsById(id);
        return productMapper.toDTO(product);
    }

    public ProductProfit listProductProfitById(long id) throws ProductNotFoundException {
        Product product = verifyIfProductExistsById(id);
        BigDecimal totalProfit = new BigDecimal(0);
        int totalStockOutcome = 0;

        List<StockTransaction> transactions = stockTransactionRepository.findTransactionsByProductOutcome(product.getCode());
        for (StockTransaction t: transactions) {
            BigDecimal transactionQuantity = BigDecimal.valueOf(t.getAmount());
            t.getValue().divide(transactionQuantity);
            BigDecimal transactionProfit = new BigDecimal(String.valueOf(t.getValue().divide(transactionQuantity)));
            transactionProfit = transactionProfit.subtract(product.getVendorsPrice());
            totalProfit = totalProfit.add(transactionProfit.multiply(transactionQuantity));
            totalStockOutcome += t.getAmount();
        }

        return ProductProfit
                .builder()
                .totalProfit(totalProfit)
                .totalStockOutcome(totalStockOutcome)
                .build();
    }

    public StockTransactionDTO findTransactionById(long id) throws TransactionNotFoundException {
        StockTransaction stockTransaction = verifyIfTransactionExists(id);
        return stockTransactionMapper.toDTO(stockTransaction);
    }

    public void deleteProductById(Long id) throws ProductNotFoundException {
        Product product = verifyIfProductExistsById(id);

        List<StockTransaction> allTransactions = stockTransactionRepository.findTransactionsByProduct(product.getCode());
        if(!(allTransactions.isEmpty())){
            for (StockTransaction transaction : allTransactions) {
                stockTransactionRepository.deleteById(transaction.getId());
            }
        }
        productRepository.deleteById(id);
    }

    public void deleteTransactionById(Long id) throws TransactionNotFoundException {
        verifyIfTransactionExists(id);

        stockTransactionRepository.deleteById(id);
    }


    public MessageResponseDTO updateProductById(Long id, ProductDTO productDTO) throws ProductNotFoundException {

        Product oldProduct = verifyIfProductExistsById(id);
        String message = "Updated Product with ID ";
        if(oldProduct.getCode() != productDTO.getCode()){
            List<StockTransaction> transactions = stockTransactionRepository.findTransactionsByProduct(oldProduct.getCode());
            int transactionsUpdated = 0;
            for (StockTransaction t: transactions) {
                t.setProduct(productDTO.getCode());
                stockTransactionRepository.save(t);
                transactionsUpdated++;
            }
            message = "Updated " +transactionsUpdated+ " transactions and the Product with ID ";
        }
        Product productToUpdate = productMapper.toModel(productDTO);
        Product updatedProduct = productRepository.save(productToUpdate);

        return createMessageResponse(updatedProduct.getId(), message);
    }

    public MessageResponseDTO updateTransactionById(Long id, StockTransactionDTO stockTransactionDTO) throws TransactionNotFoundException, ProductNotFoundException, NotEnoughInventoryException {
        verifyIfTransactionExists(id);
        StockTransaction oldTransaction = stockTransactionRepository.getById(id);
        Product newProduct = productRepository.findProductByCode(stockTransactionDTO.getProduct())
                .orElseThrow(() -> new ProductNotFoundException(" Product not found with code ", (long) stockTransactionDTO.getProduct()));

        if(oldTransaction.getTransactionType() == TransactionType.INCOME){
            newProduct.setStockAmount(newProduct.getStockAmount() - oldTransaction.getAmount());
        } else{
            newProduct.setStockAmount(newProduct.getStockAmount() + oldTransaction.getAmount());
        }
        

        stockTransactionDTO.setId(oldTransaction.getId());
        validateTransaction(stockTransactionDTO, newProduct);

        stockTransactionDTO.setTransactionDate(LocalDateTime.now().withNano(0));
        StockTransaction stockTransactionToSave = stockTransactionMapper.toModel(stockTransactionDTO);
        StockTransaction savedStockTransaction = stockTransactionRepository.save(stockTransactionToSave);

        return createMessageResponse(savedStockTransaction.getId(), "Updated Transaction with ID ");
    }

    private MessageResponseDTO createMessageResponse(Long id, String s) {
        return MessageResponseDTO
                .builder()
                .message(s + id)
                .build();
    }

    private Product verifyIfProductExistsById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID ",id));
    }

    private StockTransaction verifyIfTransactionExists(long id) throws TransactionNotFoundException {
        return stockTransactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID ",id));
    }

    private void validateTransaction(StockTransactionDTO stockTransactionDTO, Product product) throws NotEnoughInventoryException {
        
        if(stockTransactionDTO.getTransactionType() == TransactionType.INCOME){
            product.setStockAmount(product.getStockAmount() + stockTransactionDTO.getAmount());
            productRepository.save(product);
        } else{
            if(stockTransactionDTO.getAmount() <= product.getStockAmount()){
                product.setStockAmount(product.getStockAmount() - stockTransactionDTO.getAmount());
                productRepository.save(product);
            } else{
                throw new NotEnoughInventoryException(product.getId());
            }
        }
    }
}
