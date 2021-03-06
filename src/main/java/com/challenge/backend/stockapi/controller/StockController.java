package com.challenge.backend.stockapi.controller;

import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.dto.request.StockTransactionDTO;
import com.challenge.backend.stockapi.dto.response.MessageResponseDTO;
import com.challenge.backend.stockapi.entity.ProductProfit;
import com.challenge.backend.stockapi.entity.ProductQuantity;
import com.challenge.backend.stockapi.enums.ProductType;
import com.challenge.backend.stockapi.exceptions.NotEnoughInventoryException;
import com.challenge.backend.stockapi.exceptions.ProductNotFoundException;
import com.challenge.backend.stockapi.exceptions.TransactionNotFoundException;
import com.challenge.backend.stockapi.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockController {

    private StockService stockService;

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    public MessageResponseDTO createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return stockService.createProduct(productDTO);
    }


    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    public MessageResponseDTO createTransaction(@RequestBody @Valid StockTransactionDTO stockTransactionDTO) throws ProductNotFoundException, NotEnoughInventoryException {
        return stockService.createTransaction(stockTransactionDTO);
    }

    @RequestMapping(path = "/product", method = RequestMethod.GET)
    public List<ProductDTO> listAllProducts() {
        return stockService.listAllProducts();
    }

    @RequestMapping(path = "/product/type/{type}", method = RequestMethod.GET)
    public List<ProductQuantity> listAllProductsByType(@PathVariable ProductType type) {
        return stockService.listAllProductsByType(type);
    }

    @RequestMapping(path = "/transaction", method = RequestMethod.GET)
    public List<StockTransactionDTO> listAllTransactions() {
        return stockService.listAllTransactions();
    }

    @RequestMapping(path = "/transaction/{id}", method = RequestMethod.GET)
    public StockTransactionDTO findTransactionById(@PathVariable long id) throws TransactionNotFoundException {
        return stockService.findTransactionById(id);
    }

    @RequestMapping(path = "/product/profit/{id}", method = RequestMethod.GET)
    public ProductProfit listProductProfitById(@PathVariable long id) throws ProductNotFoundException {
        return stockService.listProductProfitById(id);
    }


    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET)
    public ProductDTO findProductById(@PathVariable long id) throws ProductNotFoundException {
        return stockService.findProductById(id);
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) throws ProductNotFoundException {
        stockService.deleteProductById(id);
    }

    @RequestMapping(path = "/transaction/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactionById(@PathVariable Long id) throws TransactionNotFoundException {
        stockService.deleteTransactionById(id);
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.PUT)
    public MessageResponseDTO updateProductById(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) throws ProductNotFoundException {
        return stockService.updateProductById(id, productDTO);
    }

    @RequestMapping(path = "/transaction/{id}", method = RequestMethod.PUT)
    public MessageResponseDTO updateTransactionById(@PathVariable Long id, @RequestBody @Valid StockTransactionDTO stockTransactionDTO) throws TransactionNotFoundException, ProductNotFoundException, NotEnoughInventoryException {
        return stockService.updateTransactionById(id, stockTransactionDTO);
    }

}
