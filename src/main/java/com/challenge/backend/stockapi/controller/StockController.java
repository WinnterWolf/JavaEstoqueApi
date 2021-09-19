package com.challenge.backend.stockapi.controller;

import com.challenge.backend.stockapi.dto.request.StockTransactionDTO;
import com.challenge.backend.stockapi.dto.response.MessageResponseDTO;
import com.challenge.backend.stockapi.dto.request.ProductDTO;
import com.challenge.backend.stockapi.exceptions.NotEnoughInventoryException;
import com.challenge.backend.stockapi.exceptions.ProductNotFoundException;
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

    @RequestMapping(path="/product", method = RequestMethod.POST)
    public MessageResponseDTO createProduct(@RequestBody @Valid ProductDTO productDTO){
        return stockService.createProduct(productDTO);
    }


    @RequestMapping(path="/transaction", method = RequestMethod.POST)
    public MessageResponseDTO createTransaction(@RequestBody StockTransactionDTO stockTransactionDTO) throws ProductNotFoundException, NotEnoughInventoryException {
        return stockService.createTransaction(stockTransactionDTO);
    }

    @RequestMapping(path="/product", method = RequestMethod.GET)
    public List<ProductDTO> listAllProducts(){
        return stockService.listAllProducts();
    }

    @RequestMapping(path="/transaction", method = RequestMethod.GET)
    public List<StockTransactionDTO> listAllTransactions(){
        return stockService.listAllTransactions();
    }

    @RequestMapping(path="/product/{id}", method = RequestMethod.GET)
    public ProductDTO findProductById(@PathVariable long id) throws ProductNotFoundException {
        return stockService.findProductById(id);
    }

    @RequestMapping(path="/product/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) throws ProductNotFoundException {
        stockService.deleteProductById(id);
    }

    @RequestMapping(path="/product/{id}", method = RequestMethod.PUT)
    public MessageResponseDTO updateProductByID(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) throws ProductNotFoundException {
        return stockService.updateProductById(id, productDTO);
    }

}
