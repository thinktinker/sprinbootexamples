package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Supplier;
import com.databasemapping.manytomany.repository.SupplierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // addFilter = false to disable security filter for unit test.
class SupplierControllerTest {

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
    void setup(){

        // If there is a need to delete all records before running the tests
         supplierRepository.deleteAll();

        // arrange (precondition)
        supplier1 = Supplier.builder()
                .name("Suppler1")
                .address("Address of Supplier1")
                .phone("91234567")
                .email("supplier1@gmail.com")
                .build();

        // arrange (precondition)
        supplier2 = Supplier.builder()
                .name("Suppler2")
                .address("Address of Supplier2")
                .phone("92234567")
                .email("supplier2@gmail.com")
                .build();

        supplierList.add(supplier1);
        supplierList.add(supplier2);
    }

    //Runs after each JUnit test operation
    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("** JUnit test: get all suppliers from Supplier Ctrl. **")
    void getAllSuppliers() throws Exception {

        // arrange - setup precondition
        supplierRepository.saveAll(supplierList);

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(supplierList.size())));
    }

    @Test
    @DisplayName("** JUnit test: get a supplier by id from Supplier Ctrl. **")
    void getSupplier() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), supplier1.getId()));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(supplier1.getName()))
                .andExpect(jsonPath("$.address").value(supplier1.getAddress()))
                .andExpect(jsonPath("$.phone").value(supplier1.getPhone()))
                .andExpect(jsonPath("$.email").value(supplier1.getEmail()));
    }

    @Test
    @DisplayName("** JUnit test: ResourceNotFoundException to get a supplier by id from Supplier Ctrl. **")
    void getSupplierResourceNotFoundException() throws Exception {

        // arrange - setup precondition
        supplierRepository.saveAll(supplierList);
        Long erroneousId = 1000L;

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), erroneousId));

        // assert - verify the output
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().string("{\"error\":\"Resource not found.\"}"))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("** JUnit test: save supplier from Supplier Ctrl. **")
    void saveSupplier() throws Exception {

        // arrange - setup precondition
        String requestBody = objectMapper.writeValueAsString(supplier1);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT.concat("/save"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(supplier1.getName()))
                .andExpect(jsonPath("$.address").value(supplier1.getAddress()))
                .andExpect(jsonPath("$.phone").value(supplier1.getPhone()))
                .andExpect(jsonPath("$.email").value(supplier1.getEmail()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(supplier1.getEmail())));
    }

    @Test
    @DisplayName("** JUnit test: DataIntegrityViolation(email) save supplier where email already exists from Supplier Ctrl. **")
    void saveSupplierEmailDataIntegrityViolation() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        Supplier duplicateSupplier = Supplier.builder()
                .name("Supplier1x")
                .address("Address of Supplier1x")
                .phone("91234567")
                .email("supplier1@gmail.com")
                .build();

        String requestBody = objectMapper.writeValueAsString(duplicateSupplier);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT.concat("/save"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(content().string("{\"error\":\"Email is already used.\"}"))
                .andExpect(result -> assertInstanceOf(DataIntegrityViolationException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("** JUnit test: update supplier from Supplier Ctrl. **")
    void updateSupplier() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        Supplier updateSupplier1 = supplierRepository.findById(supplier1.getId()).get();

        updateSupplier1.setName("Updated Supplier1");
        updateSupplier1.setAddress("Updated Address of Supplier1");
        updateSupplier1.setPhone("92234456");
        updateSupplier1.setEmail("updatedsuppler1@gmail.com");

        String requestBody = objectMapper.writeValueAsString(updateSupplier1);

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), updateSupplier1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updateSupplier1.getName()))
                .andExpect(jsonPath("$.address").value(updateSupplier1.getAddress()))
                .andExpect(jsonPath("$.phone").value(updateSupplier1.getPhone()))
                .andExpect(jsonPath("$.email").value(updateSupplier1.getEmail()));
     }

    @Test
    @DisplayName("** JUnit test: delete supplier from Supplier Ctrl. **")
    void deleteSupplier() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        Supplier deleteSupplier1 = supplierRepository.findById(supplier1.getId()).get();

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), deleteSupplier1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(deleteSupplier1.getName()))
                .andExpect(jsonPath("$.address").value(deleteSupplier1.getAddress()))
                .andExpect(jsonPath("$.phone").value(deleteSupplier1.getPhone()))
                .andExpect(jsonPath("$.email").value(deleteSupplier1.getEmail()));
    }

    @Test
    @DisplayName("** JUnit test: custom query get supplier by email from Supplier Ctrl. **")
    void getSuppliersByEmailLike() throws Exception {

        // arrange - setup precondition
        supplierRepository.save(supplier1);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("email", supplier1.getEmail());

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/findbyemail")).params(requestParams)
                .contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].name").value(supplier1.getName()))
                .andExpect(jsonPath("$[0].address").value(supplier1.getAddress()))
                .andExpect(jsonPath("$[0].phone").value(supplier1.getPhone()))
                .andExpect(jsonPath("$[0].email").value(supplier1.getEmail()));
    }
}

// alternate way to write the statement to verify the output
//resultActions.andExpect(status().isOk())
//        .andDo(print())
//        .andExpect(jsonPath("$.name", is (supplier1.getName())))
//        .andExpect(jsonPath("$.address", is(supplier1.getAddress())))
//        .andExpect(jsonPath("$.phone", is(supplier1.getPhone())))
//        .andExpect(jsonPath("$.email", is(supplier1.getEmail())));