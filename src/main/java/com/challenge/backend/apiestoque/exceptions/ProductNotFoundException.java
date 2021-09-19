package com.challenge.backend.apiestoque.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(Long id){
        super("Person not found with ID "+ id);
    }
}