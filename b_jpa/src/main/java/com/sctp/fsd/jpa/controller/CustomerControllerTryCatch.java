package com.martin.jpa.controller;

import com.martin.jpa.exception.ResourceNotFoundException;
import com.martin.jpa.model.Customer;
import com.martin.jpa.repository.CustomerRepository;
import com.martin.jpa.service.CustomerService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Optional;

/*
* Controller to manage incoming requests
* Using @Controller annotation
* */
@RestController
public class CustomerController {

    @Autowired  // Connect to CustomerService
    CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody Customer customer){
        try {
            // try running the code block
            Customer savedCustomer = customerService.save(customer);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        }
        catch (Exception e) {
            // present the error(s) or return the error(s)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allCustomers() throws ResourceNotFoundException {
        try{
            // try running the code block
            List<Customer> customers = (List<Customer>) customerService.findAll();

            if(customers.isEmpty())
                throw new ResourceNotFoundException();

            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch (Exception e){
            // present the errors or return the error(s)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody Customer customer) throws ResourceNotFoundException{

        try{
            // the exception here is invoked through a lambda expression ()->{}
            Customer currentCustomer = customerService.findById(id).map(foundCustomer->{
                // update the customer
                foundCustomer.setFirstName(customer.getFirstName());
                foundCustomer.setLastName(customer.getLastName());
                foundCustomer.setEmail(customer.getEmail());
                foundCustomer.setPhone(customer.getPhone());
                return customerService.save(foundCustomer);
            }).orElseThrow(()->new ResourceNotFoundException());

            return new ResponseEntity<>(currentCustomer, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") Integer id) throws NoResourceFoundException {
        try{
            // We should only delete a customer, ONLY if the customer is found
            Customer customer = customerService.findById(id).map(foundCustomer->{
                customerService.delete(foundCustomer.getId());
                return foundCustomer;
            }).orElseThrow(()->new ResourceNotFoundException());

            return new ResponseEntity<>(customer, HttpStatus.OK);

        }catch (Exception e){
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id) throws ResourceNotFoundException{
        try{
            Customer customer = customerService.findById(id).orElseThrow(()->new ResourceNotFoundException());

            return new ResponseEntity<>(customer, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getCustomerByEmailOrLastName(
            @RequestParam("email") String email,
            @RequestParam("lastName") String lastName
            ) throws ResourceNotFoundException{
        try {
            List<Customer> customers = customerService.findByEmailOrLastName(email, lastName);

            // TODO we still need to handle errors when not customer is found
            if(customers.isEmpty()){
                throw new ResourceNotFoundException("Nothing found. Check if email AND last name are entered correctly.");
            }

            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}

// Old approach for adding a customer
//    @PostMapping("/add")
//    public ResponseEntity<Object> addCustomer(
//            @RequestParam String firstName,
//            @RequestParam String lastName,
//            @RequestParam String email,
//            @Nullable @RequestParam String phone) throws Exception{
//
//        try{
//            // try running the code block
//            Customer customer = new Customer(firstName, lastName, email, phone);
//            customerRepository.save(customer);
//
//            return new ResponseEntity<>(customer, HttpStatus.CREATED);
//        }catch (Exception e){
//            // present the errors or return the error(s)
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);    // amend to remove e.getMessage()
//        }
//
//    }
