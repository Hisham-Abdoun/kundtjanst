package org.example.kundtjanst;

import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class KundtjanstApplicationTests {

    @Autowired
    private CustomerService customerService;

    @Test
    void contextLoads() {
    }

    @Test
    void printAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        System.out.println("=== Alla kunder ===");
        for (CustomerDto customer : customers) {
            System.out.println("ID: " + customer.getId() +
                    ", Namn: " + customer.getFirstName() + " " + customer.getLastName() +
                    ", E-post: " + customer.getEmail() +
                    ", Telefon: " + customer.getPhoneNumber());
        }
        System.out.println("Antal kunder: " + customers.size());
    }

}
