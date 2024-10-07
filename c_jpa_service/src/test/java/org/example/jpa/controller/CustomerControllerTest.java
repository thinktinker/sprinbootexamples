package org.example.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jpa.model.Customer;
import org.example.jpa.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // addFilter = false to disable security filter for unit test.
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/customers";


    private Customer customer1, customer2;
    private final List<Customer> customerList = new ArrayList<>();

    //Runs before each JUnit test operation
    @BeforeEach
    void setup(){

        // If there is a need to delete all records before running the tests
        customerRepository.deleteAll();

        // arrange (precondition)
        customer1 = Customer.builder()
                .name("Customer X")
                .email("customer_x@gmail.com")
                .phone("91234567")
                .build();

        // arrange (precondition)
        customer2 = Customer.builder()
                .name("Customer Y")
                .email("customer_y@gmail.com")
                .phone("98765432")
                .build();

        customerList.add(customer1);
        customerList.add(customer2);
    }

    //Runs after each JUnit test operation
    @AfterEach
    void tearDown() {
    }

    @Test
    void allCustomers() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        customerRepository.saveAll(customerList);

        // assert - verify the output
        List<Customer> suppliers = customerRepository.findAll();
        assertFalse(suppliers.isEmpty());
        assertEquals(suppliers.size(), customerList.size());
    }

    @Test
    void saveCustomer() throws Exception {

        // arrange - setup precondition
        String requestBody = objectMapper.writeValueAsString(customer1);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(customer1.getName()))
                .andExpect(jsonPath("$.email").value(customer1.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer1.getPhone()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(customer1.getEmail())));
    }

    @Test
    void updateCustomer() throws Exception {

        // arrange - setup precondition
        customerRepository.save(customer1);

        Customer updateCustomer1 = customerRepository.findById(customer1.getId()).get();

        updateCustomer1.setName("Updated Customer X");
        updateCustomer1.setPhone("92234567");
        updateCustomer1.setEmail("updated_customer_x@gmail.com");

        String requestBody = objectMapper.writeValueAsString(updateCustomer1);

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), updateCustomer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updateCustomer1.getName()))
                .andExpect(jsonPath("$.phone").value(updateCustomer1.getPhone()))
                .andExpect(jsonPath("$.email").value(updateCustomer1.getEmail()));
    }

    @Test
    void deleteCustomer() throws Exception {

        // arrange - setup precondition
        customerRepository.save(customer1);

        Customer deleteCustomer1 = customerRepository.findById(customer1.getId()).get();

        String expectedResponse = String.format("%s deleted successfully", deleteCustomer1.getName());

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), deleteCustomer1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                // Checking that the response body matches the expected message
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));
    }

    @Test
    void countCustomer() {

            // arrange - setup precondition
            customerRepository.saveAll(customerList);

            // act - action or behaviour to test
            long count = customerRepository.count();

            // assert - verify the output
            assertEquals(customerList.size(), count, "Number of customers should match the saved customers.");

    }
}