package com.challenge.backend.apiestoque.controller;

import com.challenge.backend.apiestoque.dto.MessageResponseDTO;
import com.challenge.backend.apiestoque.dto.request.ProductDTO;
import com.challenge.backend.apiestoque.entity.Product;
import com.challenge.backend.apiestoque.entity.StockTransaction;
import com.challenge.backend.apiestoque.enums.TransactionType;
import com.challenge.backend.apiestoque.repository.ProductRepository;
import com.challenge.backend.apiestoque.repository.StockTransactionRepository;
import com.challenge.backend.apiestoque.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @RequestMapping(path="/product", method = RequestMethod.POST)
    public MessageResponseDTO createProduct(@RequestBody @Valid ProductDTO productDTO){
        return stockService.createProduct(productDTO);
    }

    //TODO
    @RequestMapping(path="/transaction", method = RequestMethod.POST)
    public MessageResponseDTO createTransaction(@RequestBody StockTransaction stockTransaction){
        return stockService.createTransaction(stockTransaction);
    }

    @RequestMapping(path="/product", method = RequestMethod.GET)
    public List<ProductDTO> listAll(){
        return stockService.listAll();
    }

}
