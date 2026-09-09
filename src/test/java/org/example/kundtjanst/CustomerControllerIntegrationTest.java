package org.example.kundtjanst;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kundtjanst.controller.CustomerController;
import org.example.kundtjanst.dto.CustomerDto;
import org.example.kundtjanst.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerIntegrationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setFirstName("Test");
        newCustomer.setLastName("Person");
        newCustomer.setEmail("test.person@example.com");
        newCustomer.setPhoneNumber("1234567890");

        CustomerDto createdCustomer = new CustomerDto();
        createdCustomer.setId(1L);
        createdCustomer.setFirstName("Test");
        createdCustomer.setLastName("Person");
        createdCustomer.setEmail("test.person@example.com");
        createdCustomer.setPhoneNumber("1234567890");

        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(createdCustomer);

        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CustomerDto responseCustomer = objectMapper.readValue(response, CustomerDto.class);
        
        assertNotNull(responseCustomer.getId());
        assertEquals("Test", responseCustomer.getFirstName());
        assertEquals("Person", responseCustomer.getLastName());
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setFirstName("Get");
        customer.setLastName("Test");
        customer.setEmail("get.test@example.com");
        customer.setPhoneNumber("0987654321");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Get"));
    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(99999L)).thenThrow(new RuntimeException("Kund hittades inte"));

        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDto updateCustomer = new CustomerDto();
        updateCustomer.setFirstName("Updated");
        updateCustomer.setLastName("Name");
        updateCustomer.setEmail("updated.name@example.com");
        updateCustomer.setPhoneNumber("2222222222");

        CustomerDto updatedCustomer = new CustomerDto();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("Updated");
        updatedCustomer.setLastName("Name");
        updatedCustomer.setEmail("updated.name@example.com");
        updatedCustomer.setPhoneNumber("2222222222");

        when(customerService.updateCustomer(eq(1L), any(CustomerDto.class))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    void deleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
