package org.example.kundtjanst.client;

import org.example.kundtjanst.dto.RoomDto;
import org.example.kundtjanst.exception.BookingServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class RoomServiceClient {
    private final RestClient restClient;
    private final String bookingServiceUrl;

    public RoomServiceClient(
            RestClient restClient,
            @Value("${booking.service.url}") String bookingServiceUrl
    ) {
        this.restClient = restClient;
        this.bookingServiceUrl = bookingServiceUrl;
        System.out.println("RoomServiceClient initialized with URL: " + bookingServiceUrl);
    }

    /**
     * Kolla om ett rum finns
     */
    public boolean isRoomAvailable(Long roomId)
    {
        try {
            restClient.get()
                    .uri(bookingServiceUrl + "/api/rooms/" + roomId)
                    .retrieve()
                    .toEntity(RoomDto.class);

            return true; // 200 OK → rummet finns

        } catch (HttpClientErrorException.NotFound e) {
            return false; // 404 → rummet finns inte

        } catch (Exception e) {
            throw new BookingServiceUnavailableException("Bokningstjänst är inte tillgänglig", e);
        }
    }

    /**
     * Hämta alla rum från booking service.
     */
    public List<RoomDto> getAllRooms() {
        String fullUrl = bookingServiceUrl + "/api/rooms";
        System.out.println("Calling booking service at: " + fullUrl);
        try {
            return restClient.get()
                    .uri(fullUrl)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<RoomDto>>() {});
        }
        catch (HttpClientErrorException e)
        {
            System.out.println("HttpClientErrorException: " + e.getMessage());
            throw new RuntimeException("Kunde inte hämta rum från booking service", e);
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new BookingServiceUnavailableException("Bokningstjänst är inte tillgänglig", e);
        }
    }
}
