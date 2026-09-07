package org.example.kundtjanst.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rumsnummer krävs")
    private int roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rumstyp krävs")
    private org.example.kundtjanst.model.RoomType roomType;

    @Min(value = 0, message = "Minst 0")
    @Max(value = 2, message = "Max 2 extra sängar")
    private int extraBeds;

    @NotNull(message = "Pris krävs")
    private double pricePerNight;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public org.example.kundtjanst.model.RoomType getRoomType() { return roomType; }
    public void setRoomType(org.example.kundtjanst.model.RoomType roomType) { this.roomType = roomType; }

    public int getExtraBeds() { return extraBeds; }
    public void setExtraBeds(int extraBeds) { this.extraBeds = extraBeds; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
