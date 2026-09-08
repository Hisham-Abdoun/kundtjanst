package org.example.kundtjanst.exception;

public class BookingServiceUnavailableException extends RuntimeException {

    public BookingServiceUnavailableException(String message) {
        super(message);
    }
}
