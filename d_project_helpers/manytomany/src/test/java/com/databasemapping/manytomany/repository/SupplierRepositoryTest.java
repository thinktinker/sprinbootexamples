package com.databasemapping.manytomany.repository;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Supplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // addFilter = false to disable security filter for unit test.
class SupplierRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/admin/api/supplier";

    private Supplier supplier1, supplier2;
    private final List<Supplier> supplierList = new ArrayList<>();



    //Runs before each JUnit test operation
    @BeforeEach
    void setUp() {
        // If there is a need to delete all records before running the tests
        supplierRepository.deleteAll();

        LocalDateTime timeNow = LocalDateTime.now();

        // arrange (precondition)
        supplier1 = Supplier.builder()
                .name("Suppler1")
                .address("Address of Supplier1")
                .phone("91234567")
                .email("supplier1@gmail.com")
                .dateAdded(timeNow)
                .dateUpdated(timeNow)
                .build();

        // arrange (precondition)
        supplier2 = Supplier.builder()
                .name("Suppler2")
                .address("Address of Supplier2")
                .phone("92234567")
                .email("supplier2@gmail.com")
                .dateAdded(timeNow)
                .dateUpdated(timeNow)
                .build();

        supplierList.add(supplier1);
        supplierList.add(supplier2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("** JUnit test: get all suppliers from Supplier Repo. **")
    void getAllSuppliers() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        supplierRepository.saveAll(supplierList);

        // assert - verify the output
        List<Supplier> suppliers = supplierRepository.findAll();
        assertFalse(suppliers.isEmpty());
        assertEquals(suppliers.size(), supplierList.size());
    }

    @Test
    @DisplayName("** JUnit test: get a supplier by id from Supplier Repo. **")
    void getSupplier() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        supplierRepository.save(supplier1);

        // assert - verify the output
        Supplier supplier = supplierRepository.findById(supplier1.getId()).get();
        assertNotNull(supplier);
        assertEquals(supplier1, supplier);
    }

    @Test
    @DisplayName("** JUnit test: NoSuchElementException to get a supplier by id from Supplier Repo. **")
    void getSupplierNoSuchElementException() throws Exception {

        // arrange - setup precondition
        supplierRepository.saveAll(supplierList);
        Long erroneousId = 1000L;

        // act -  action or behaviour to test
        assertThrows(NoSuchElementException.class, () -> {
            Supplier erroneousSupplier = supplierRepository.findById(erroneousId).get();
        });

        // assert - verify the output
        assertEquals(2, supplierRepository.count());
    }

    @Test
    @DisplayName("** JUnit test: save supplier from Supplier Repo. **")
    void saveSupplier() throws Exception {

        // arrange - setup precondition
        // refer to setup()

        // act - action or behaviour to test
        Supplier savedSupplier = supplierRepository.save(supplier1);

        // assert - verify the output
        assertNotNull(savedSupplier);
        assertNotEquals(savedSupplier.getId() , 0);
    }

    @Test
    @DisplayName("** JUnit test: DataIntegrityViolation(email) save supplier rom Supplier Repo. **")
    void saveSupplierEmailDataIntegrityViolation() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        // act -  action or behaviour to test
        assertThrows(DataIntegrityViolationException.class, () -> {
            LocalDateTime timeNow = LocalDateTime.now();
            Supplier duplicateSupplier = Supplier.builder()
                    .name("Supplier1x")
                    .address("Address of Supplier1x")
                    .phone("91234567")
                    .email("supplier1@gmail.com")
                    .dateAdded(timeNow)
                    .dateUpdated(timeNow)
                    .build();
            supplierRepository.save(duplicateSupplier);
        });

        // Tip: Try always to verify the side effects
        assertEquals(1, supplierRepository.count());
    }

    @Test
    @DisplayName("** JUnit test: update supplier from Supplier Repo. **")
    void updateSupplier() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        // act -  action or behaviour to test
        Supplier updateSupplier1 = supplierRepository.findById(supplier1.getId()).get();

        LocalDateTime timeNow = LocalDateTime.now();
        updateSupplier1.setName("Updated Supplier1");
        updateSupplier1.setAddress("Updated Address of Supplier1");
        updateSupplier1.setPhone("92234456");
        updateSupplier1.setEmail("updatedsuppler1@gmail.com");
        updateSupplier1.setDateUpdated(timeNow);

        supplierRepository.save(updateSupplier1);

        // assert - verify the output
        Optional<Supplier> updatedSupplier1 = supplierRepository.findById(updateSupplier1.getId());

        assertNotNull(updatedSupplier1);
        assertEquals(updateSupplier1.getEmail(), updatedSupplier1.get().getEmail());
        assertEquals(updateSupplier1.getAddress(), updatedSupplier1.get().getAddress());
        assertEquals(updateSupplier1.getPhone(), updatedSupplier1.get().getPhone());
        assertEquals(updateSupplier1.getEmail(), updatedSupplier1.get().getEmail());
        assertEquals(updateSupplier1.getDateAdded(), updatedSupplier1.get().getDateAdded());
        assertEquals(updateSupplier1.getDateUpdated(), updatedSupplier1.get().getDateUpdated());
    }

    @Test
    @DisplayName("** JUnit test: delete supplier from Supplier Repo. **")
    void deleteSupplier() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);
        Supplier deleteSupplier1 = supplierRepository.findById(supplier1.getId()).get();

        // act -  action or behaviour to test
        supplierRepository.deleteById(supplier1.getId());

        // assert - verify the output
        Optional<Supplier> deletedSupplier1 = supplierRepository.findById(deleteSupplier1.getId());
        assertTrue(deletedSupplier1.isEmpty());
    }

    @Test
    @DisplayName("** JUnit test: custom query get supplier by email from Supplier Repo. **")
    void findEmailLike() {

        // arrange - setup precondition
        supplierRepository.saveAll(supplierList);

        // act -  action or behaviour to test
        List<Supplier> suppliers = supplierRepository.findEmailLike(supplier1.getEmail());

        // assert - verify the output
        assertFalse(suppliers.isEmpty());
        assertThat(suppliers, hasItem(allOf(
                hasProperty("email", is(supplier1.getEmail()))
        )));
        assertEquals(suppliers.size(), 1);
    }

    @Test
    @DisplayName("** JUnit test: custom query get supplier by name from Supplier Repo. **")
    void findByNameContaining() {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        // act -  action or behaviour to test
        List<Supplier> suppliers = supplierRepository.findByNameContaining(supplier1.getName());

        // assert - verify the output
        assertFalse(suppliers.isEmpty());
        assertEquals(suppliers.size(), 1);
    }

}