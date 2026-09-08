package org.example.kundtjanst.client;

import org.example.kundtjanst.exception.BookingServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

// Ansvarar för all kommunikation med bokningstjänsten via REST.
@Component
public class BookingClient {

    private final RestClient restClient;

    public BookingClient(@Value("${pensionat.api.url}") String pensionatUrl) {
        this.restClient = RestClient.create(pensionatUrl);
    }

    // Frågar bokningstjänsten om kunden har några bokningar.
    // Kastar BookingServiceUnavailableException om bokningstjänsten inte svarar.
    public boolean hasBookings(Long customerId) {
        try {
            Boolean result = restClient.get()
                    .uri("/customer/{customerId}/exists", customerId)
                    .retrieve()
                    .body(Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (ResourceAccessException ex) {
            throw new BookingServiceUnavailableException(
                    "Kunde inte nå bokningstjänsten just nu. Försök igen senare.");
        }
    }
}
