package org.example.kundtjanst.dto;

import jakarta.validation.constraints.*;
import org.example.kundtjanst.model.RoomType;

/**
 * DTO för rumshantering.
 * Här finns API-endpoint:er för att hantera rumshantering.
 * DENNA BEHÖVS LÄNGRE INTE.
 */
public class RoomDto {

    private Long id;

    @NotNull(message = "Rumsnummer krävs")
    private int roomNumber;

    @NotNull(message = "Rumstyp krävs")
    private RoomType roomType;

    @Min(value = 0, message = "Minst 0")
    @Max(value = 2, message = "Max 2 extra sängar")
    private int extraBeds;

    @NotNull(message = "Pris krävs")
    private double pricePerNight;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public int getExtraBeds() { return extraBeds; }
    public void setExtraBeds(int extraBeds) { this.extraBeds = extraBeds; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
}
