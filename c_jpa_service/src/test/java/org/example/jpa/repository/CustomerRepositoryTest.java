package org.example.jpa.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jpa.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // addFilter = false to disable security filter for unit test.
class CustomerRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/admin/api";

    private Customer customer1, customer2;
    private final List<Customer> customerList = new ArrayList<>();

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

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("** JUnit test: get all customers from Customer Repo. **")
    void getAllCustomers() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        customerRepository.saveAll(customerList);

        // assert - verify the output
        List<Customer> customers = customerRepository.findAll();
        assertFalse(customers.isEmpty());
        assertEquals(customers.size(), customerList.size());
    }

    @Test
    @DisplayName("** JUnit test: get a customer by id from Customer Repo. **")
    void getCustomer() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        customerRepository.save(customer1);

        // assert - verify the output
        Customer customer = customerRepository.findById(customer1.getId()).get();
        assertNotNull(customer);
        assertEquals(customer1, customer);
    }

    @Test
    @DisplayName("** JUnit test: save customer from Customer Repo. **")
    void saveCustomer() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act - action or behaviour to test
        Customer savedCustomer = customerRepository.save(customer1);

        // assert - verify the output
        assertNotNull(savedCustomer);
        assertNotEquals(savedCustomer.getId() , 0);
    }

    @Test
    @DisplayName("** JUnit test: update customer from Customer Repo. **")
    void updateCustomer() throws Exception {

        // arrange - setup precondition
        customerRepository.save(customer1);

        // act -  action or behaviour to test
        Customer updateCustomer1 = customerRepository.findById(customer1.getId()).get();

        updateCustomer1.setName("Updated Customer X");
        updateCustomer1.setEmail("updated_customer_x@gmail.com");
        updateCustomer1.setPhone("92234567");

        customerRepository.save(updateCustomer1);

        // assert - verify the output
        Optional<Customer> updatedCustomer1 = customerRepository.findById(updateCustomer1.getId());

        assertNotNull(updatedCustomer1);
        assertEquals(updateCustomer1.getName(), updatedCustomer1.get().getName());
        assertEquals(updateCustomer1.getEmail(), updatedCustomer1.get().getEmail());
        assertEquals(updateCustomer1.getPhone(), updatedCustomer1.get().getPhone());
    }


    @Test
    @DisplayName("** JUnit test: delete customer from Customer Repo. **")
    void deleteCustomer() throws Exception {

        // arrange - setup precondition
        customerRepository.save(customer1);
        Customer deleteCustomer1 = customerRepository.findById(customer1.getId()).get();

        // act -  action or behaviour to test
        customerRepository.deleteById(customer1.getId());

        // assert - verify the output
        Optional<Customer> deletedSupplier1 = customerRepository.findById(deleteCustomer1.getId());
        assertTrue(deletedSupplier1.isEmpty());
    }

}