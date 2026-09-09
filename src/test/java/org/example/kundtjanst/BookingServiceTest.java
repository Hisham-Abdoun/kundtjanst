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
        Long bookingId = 4L;
        boolean isAvailable = bookingServiceClient.isBookingAvailable(bookingId);
        assertTrue(isAvailable);
    }

    @Test
    void isServiceUp()
    {
        boolean isUp = bookingServiceClient.isServiceUp();
        System.out.println("Booking service är uppe: " + isUp);
        assertTrue(isUp, "Booking service bör vara uppe för att köra detta test");
    }
}
