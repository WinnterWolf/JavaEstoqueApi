package com.challenge.backend.stockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughInventoryException extends Exception {
    public NotEnoughInventoryException(long id) {
        super("Not enough product with ID "+ id +" on inventory to complete the transaction");
    }
}
