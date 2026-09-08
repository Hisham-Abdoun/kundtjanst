package org.example.kundtjanst.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Kunden med id " + id + " hittades inte");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}