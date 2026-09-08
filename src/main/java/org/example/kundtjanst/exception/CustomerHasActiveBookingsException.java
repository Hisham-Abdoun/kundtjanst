package org.example.kundtjanst.exception;

public class CustomerHasActiveBookingsException extends RuntimeException {

    public CustomerHasActiveBookingsException(Long customerId) {
        super("Kunden med id " + customerId + " har aktiva bokningar och kan inte tas bort");
    }
}
