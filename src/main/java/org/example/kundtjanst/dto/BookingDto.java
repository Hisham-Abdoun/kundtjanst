package org.example.kundtjanst.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BookingDto {

    private Long id;

    @NotNull(message = "Startdatum krävs")
    private LocalDate startDate;

    @NotNull(message = "Slutdatum krävs")
    private LocalDate endDate;

    @Min(value = 1, message = "Minst 1 gäst")
    private int numberOfGuests;

    @AssertTrue(message = "Slutdatum måste vara efter startdatum")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true; // @NotNull hanterar detta
        }
        return endDate.isAfter(startDate);
    }

    @NotNull(message = "Kund måste väljas")
    private Long customerId;
    private String customerName;

    @NotNull(message = "Rum måste väljas")
    private Long roomId;
    private int roomNumber;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
}
