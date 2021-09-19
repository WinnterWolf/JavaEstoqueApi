package com.challenge.backend.stockapi.service;

import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.dto.request.StockTransactionDTO;
import com.challenge.backend.stockapi.dto.response.MessageResponseDTO;
import com.challenge.backend.stockapi.entity.Product;
import com.challenge.backend.stockapi.entity.StockTransaction;
import com.challenge.backend.stockapi.enums.TransactionType;
import com.challenge.backend.stockapi.exceptions.NotEnoughInventoryException;
import com.challenge.backend.stockapi.exceptions.ProductNotFoundException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        TransactionType income = TransactionType.INCOME;
        if(stockTransactionDTO.getTransactionType() == income){
            product.setStockAmount(product.getStockAmount()+stockTransactionDTO.getAmount());
            productRepository.save(product);
        } else{
            if(stockTransactionDTO.getAmount() <= product.getStockAmount()){
                product.setStockAmount(product.getStockAmount()-stockTransactionDTO.getAmount());
                productRepository.save(product);
            } else{
                throw new NotEnoughInventoryException(product.getId());
            }
        }

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

    public void deleteProductById(Long id) throws ProductNotFoundException {
        Product product = verifyIfProductExistsById(id);

//        TODO verificar se existem transações com esse produto e exclui-las também.
        productRepository.deleteById(id);
    }


    public MessageResponseDTO updateProductById(Long id, ProductDTO productDTO) throws ProductNotFoundException {

        verifyIfProductExistsById(id);
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

    private Product verifyIfProductExistsById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code ",id));
    }


}
