package com.RentABook.utilPack;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    public ResponseEntity<?> handlerException(Exception e) {
        if (e instanceof NullPointerException){
            return new ResponseEntity<>(new com.RentABook.utilPack.ErrorResponse("Null pointer exception occurred",400),HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            return new ResponseEntity<>(new com.RentABook.utilPack.ErrorResponse("Invalid argument exception occurred",400),HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred : "+e.getMessage(),500),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
