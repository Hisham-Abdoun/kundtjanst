package org.example.kundtjanst.client;

import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.dto.BookingDto;
import org.example.kundtjanst.dto.RoomDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class BookingServiceClient {

    private final RestClient restClient;
    private final String bookingServiceUrl;

    public BookingServiceClient(
            RestClient restClient,
            @Value("${booking.service.url}") String bookingServiceUrl
    ) {
        this.restClient = restClient;
        this.bookingServiceUrl = bookingServiceUrl;
        System.out.println("BookingServiceClient initialized with URL: " + bookingServiceUrl);
    }

    /**
     * Kolla om en bokning finns.
     */
    public boolean isBookingAvailable(Long bookingId)
    {
        try {
            restClient.get()
                    .uri(bookingServiceUrl + "/api/bookings/" + bookingId)
                    .retrieve()
                    .toEntity(BookingDto.class);

            return true; // 200 OK → bokningen finns

        } catch (HttpClientErrorException.NotFound e) {
            return false; // 404 → bokningen finns inte

        } catch (Exception e) {
            throw new RuntimeException("Bokningsstjänst är inte tillgänglig");
        }
    }

    /**
     * Hämta alla bokningar från booking service.
     */
    public List<BookingDto> getAllBookings() {
        String fullUrl = bookingServiceUrl + "/api/bookings";
        System.out.println("Calling booking service at: " + fullUrl);
        try {
            return restClient.get()
                    .uri(fullUrl)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<BookingDto>>() {});
        }
        catch (HttpClientErrorException e)
        {
            System.out.println("HttpClientErrorException: " + e.getMessage());
            throw new RuntimeException("Kunde inte hämta bokningar från booking service", e);
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Bokningstjänst är inte tillgänglig", e);
        }
    }
}
