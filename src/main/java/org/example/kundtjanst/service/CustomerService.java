package org.example.kundtjanst.service;

import org.example.kundtjanst.client.BookingClient;
import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.exception.CustomerHasActiveBookingsException;
import org.example.kundtjanst.exception.CustomerNotFoundException;
import org.example.kundtjanst.model.Customer;
import org.example.kundtjanst.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingClient bookingClient;

    public CustomerService(CustomerRepository customerRepository,
                           BookingClient bookingClient) {
        this.customerRepository = customerRepository;
        this.bookingClient = bookingClient;
    }

    // Konvertera Entity -> DTO
    private CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        return dto;
    }

    // Konvertera DTO -> Entity
    private Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customer;
    }

    // Hämta alla kunder
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Hämta en kund via ID
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return toDto(customer);
    }

    // Registrera ny kund
    public CustomerDto saveCustomer(CustomerDto dto) {
        Customer saved = customerRepository.save(toEntity(dto));
        return toDto(saved);
    }

    // Ändra en kunds uppgifter
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());

        Customer updated = customerRepository.save(customer);
        return toDto(updated);
    }

    // Ta bort en kund
    // Frågar bokningstjänsten om kunden har bokningar innan kunden tas bort.
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        boolean hasBookings = bookingClient.hasBookings(id);
        if (hasBookings) {
            throw new CustomerHasActiveBookingsException(id);
        }

        customerRepository.delete(customer);
    }
}