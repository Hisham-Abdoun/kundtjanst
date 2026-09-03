package org.example.kundtjanst.service;

import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.model.Customer;
import org.example.kundtjanst.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kund hittades inte med id: " + id));
        return toDto(customer);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return toDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kund hittades inte med id: " + id));

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhoneNumber(customerDto.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return toDto(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Kund hittades inte med id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        return dto;
    }

    private Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customer;
    }
}
