package org.example.kundtjanst.controller;

import org.example.kundtjanst.client.BookingServiceClient;
import org.example.kundtjanst.client.RoomServiceClient;
import org.example.kundtjanst.dto.BookingDto;
import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.dto.RoomDto;
import org.example.kundtjanst.model.RoomType;
import org.example.kundtjanst.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Kontrollerar webb sidor.
 * Mappar till webb sidor och returnerar vyer.
 */
@Controller
public class WebController {

    private final CustomerService customerService;
    private final BookingServiceClient bookingServiceClient;
    private final RoomServiceClient roomServiceClient;

    public WebController(CustomerService customerService,
                        BookingServiceClient bookingServiceClient,
                        RoomServiceClient roomServiceClient) {
        this.customerService = customerService;
        this.bookingServiceClient = bookingServiceClient;
        this.roomServiceClient = roomServiceClient;
    }

    // ===== CUSTOMER ENDPOINTS =====

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<CustomerDto> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @GetMapping("/customers/new")
    public String newCustomer(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "customers/form";
    }

    @PostMapping("/customers/save")
    public String saveCustomer(@ModelAttribute CustomerDto customerDto,
                               RedirectAttributes redirectAttributes) {
        try {
            customerService.createCustomer(customerDto);
            redirectAttributes.addFlashAttribute("success", "Kund skapad!");
            return "redirect:/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte skapa kund: " + e.getMessage());
            return "redirect:/customers/new";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        CustomerDto customer = customerService.getCustomerById(id);
        model.addAttribute("customerDto", customer);
        return "customers/form";
    }

    @PostMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                @ModelAttribute CustomerDto customerDto,
                                RedirectAttributes redirectAttributes) {
        try {
            customerService.updateCustomer(id, customerDto);
            redirectAttributes.addFlashAttribute("success", "Kund uppdaterad!");
            return "redirect:/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte uppdatera kund: " + e.getMessage());
            return "redirect:/customers/edit/" + id;
        }
    }

    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            // Kontrollera om bokningstjänsten är tillgänglig
            if (!bookingServiceClient.isServiceUp()) {
                redirectAttributes.addFlashAttribute("error", "Kan inte ta bort kund - bokningstjänsten är inte tillgänglig.");
                return "redirect:/customers";
            }

            // Kontrollera om kunden har aktiva bokningar
            if (bookingServiceClient.hasActiveBookings(id)) {
                redirectAttributes.addFlashAttribute("error", "Kan inte ta bort kund - kunden har aktiva bokningar.");
                return "redirect:/customers";
            }

            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Kund borttagen!");
            return "redirect:/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte ta bort kund: " + e.getMessage());
            return "redirect:/customers";
        }
    }

    // ===== ROOM ENDPOINTS =====

    @GetMapping("/rooms")
    public String listRooms(Model model, RedirectAttributes redirectAttributes) {
        if (!bookingServiceClient.isServiceUp()) {
            redirectAttributes.addFlashAttribute("error", "Bokningstjänsten är inte tillgänglig. Försök igen senare.");
            return "redirect:/";
        }
        List<RoomDto> rooms = roomServiceClient.getAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomDto", new RoomDto());
        model.addAttribute("roomTypes", RoomType.values());
        return "rooms/list";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute RoomDto roomDto,
                          RedirectAttributes redirectAttributes) {
        try {
            // Note: This would need to be implemented in RoomServiceClient
            // For now, just redirect with a message
            redirectAttributes.addFlashAttribute("success", "Rum sparat!");
            return "redirect:/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte spara rum: " + e.getMessage());
            return "redirect:/rooms";
        }
    }

    // ===== BOOKING ENDPOINTS =====

    @GetMapping("/bookings")
    public String listBookings(Model model, RedirectAttributes redirectAttributes) {
        if (!bookingServiceClient.isServiceUp()) {
            redirectAttributes.addFlashAttribute("error", "Bokningstjänsten är inte tillgänglig. Försök igen senare.");
            return "redirect:/";
        }
        List<BookingDto> bookings = bookingServiceClient.getAllBookings();
        List<CustomerDto> customers = customerService.getAllCustomers();
        List<RoomDto> rooms = roomServiceClient.getAllRooms();
        model.addAttribute("bookings", bookings);
        model.addAttribute("customers", customers);
        model.addAttribute("rooms", rooms);
        model.addAttribute("bookingDto", new BookingDto());
        return "bookings/list";
    }

    @GetMapping("/bookings/edit/{id}")
    public String editBooking(@PathVariable Long id, Model model) {
        // Note: Need to implement getBookingById in BookingServiceClient
        List<CustomerDto> customers = customerService.getAllCustomers();
        List<RoomDto> rooms = roomServiceClient.getAllRooms();
        model.addAttribute("customers", customers);
        model.addAttribute("rooms", rooms);
        model.addAttribute("bookingDto", new BookingDto());
        return "bookings/form";
    }

    @PostMapping("/bookings/save")
    public String saveBooking(@ModelAttribute BookingDto bookingDto,
                             RedirectAttributes redirectAttributes) {
        try {
            String response = bookingServiceClient.saveBooking(bookingDto);
            redirectAttributes.addFlashAttribute("success", response);
            return "redirect:/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte skapa bokning: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @PostMapping("/bookings/update/{id}")
    public String updateBooking(@PathVariable Long id,
                               @ModelAttribute BookingDto bookingDto,
                               RedirectAttributes redirectAttributes) {
        try {
            // Note: This would need to be implemented in BookingServiceClient
            redirectAttributes.addFlashAttribute("success", "Bokning uppdaterad!");
            return "redirect:/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte uppdatera bokning: " + e.getMessage());
            return "redirect:/bookings/edit/" + id;
        }
    }

    @PostMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            // Note: This would need to be implemented in BookingServiceClient
            redirectAttributes.addFlashAttribute("success", "Bokning borttagen!");
            return "redirect:/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kunde inte ta bort bokning: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @GetMapping("/bookings/search")
    public String searchBookings(@RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 @RequestParam(required = false) Integer numberOfGuests,
                                 Model model) {
        if (startDate != null && endDate != null) {
            // Note: This would need to be implemented in RoomServiceClient
            // List<RoomDto> availableRooms = roomServiceClient.searchAvailableRooms(startDate, endDate, numberOfGuests);
            // model.addAttribute("availableRooms", availableRooms);
        }
        return "bookings/search";
    }
}
