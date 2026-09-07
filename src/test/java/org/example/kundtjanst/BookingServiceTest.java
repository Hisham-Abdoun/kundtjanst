package org.example.kundtjanst;

import org.example.kundtjanst.client.BookingServiceClient;
import org.example.kundtjanst.dto.BookingDto;
import org.example.kundtjanst.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest
{
    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Test
    void getBookingById()
    {
        Long bookingId = 13L;
        boolean isAvailable = bookingServiceClient.isBookingAvailable(bookingId);
        assertTrue(isAvailable);
    }

    @Test
    void getAllBookings()
    {
        List<BookingDto> bookings = bookingServiceClient.getAllBookings();
        // System.out.println("Bokningar: " + bookings);
        System.out.println("=== Alla Bokningar ===");
        for (BookingDto books : bookings) {
            System.out.println("ID: " + books.getId() +
                    ", Namn: " + books.getCustomerName() +
                    ", Rum: " + books.getRoomNumber());
        }
    }
}
