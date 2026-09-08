package org.example.kundtjanst;

import org.example.kundtjanst.client.BookingClient;
import org.example.kundtjanst.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

// Integrationstester som gör riktiga HTTP-anrop mot kundtjänstens API.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @LocalServerPort
    private int port;

    // Skapas manuellt istället för att förlita sig på Spring Boots auto-registrerade bean.
    private final RestTemplate restTemplate = new RestTemplate();

    // Mockar anropet till bokningstjänsten, så testet är oberoende av pensionat.
    @MockitoBean
    private BookingClient bookingClient;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // Test 1: Registrera en giltig kund -> 201 Created
    @Test
    void createCustomer_withValidData_returns201() {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName("Erik");
        dto.setLastName("Svensson");
        dto.setEmail("erik.svensson@example.se");
        dto.setPhoneNumber("070-555 44 33");

        ResponseEntity<CustomerDto> response = restTemplate.postForEntity(
                url("/api/customers"), dto, CustomerDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Erik", response.getBody().getFirstName());
    }

    // Test 2: Registrera en kund med ogiltig data (tomt förnamn) -> 400 Bad Request
    @Test
    void createCustomer_withInvalidData_returns400() {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName(""); // Ogiltigt - @NotBlank
        dto.setLastName("Svensson");
        dto.setEmail("invalid-format-not-email"); // Ogiltigt - @Email
        dto.setPhoneNumber("070-555 44 33");

        try {
            restTemplate.postForEntity(url("/api/customers"), dto, String.class);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            return;
        }
        throw new AssertionError("Förväntade ett 400 Bad Request, men inget kastades");
    }

    // Test 3: Ta bort en kund som har aktiva bokningar -> 409 Conflict
    @Test
    void deleteCustomer_withActiveBookings_returns409() {
        // Skapa en kund att testa med
        CustomerDto dto = new CustomerDto();
        dto.setFirstName("Maria");
        dto.setLastName("Karlsson");
        dto.setEmail("maria.karlsson@example.se");
        dto.setPhoneNumber("070-111 00 00");
        ResponseEntity<CustomerDto> created = restTemplate.postForEntity(
                url("/api/customers"), dto, CustomerDto.class);
        Long customerId = created.getBody().getId();

        // Simulera att bokningstjänsten säger att kunden har bokningar
        when(bookingClient.hasBookings(customerId)).thenReturn(true);

        try {
            restTemplate.exchange(
                    url("/api/customers/" + customerId),
                    HttpMethod.DELETE,
                    null,
                    String.class);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            return;
        }
        throw new AssertionError("Förväntade ett 409 Conflict, men inget kastades");
    }
}