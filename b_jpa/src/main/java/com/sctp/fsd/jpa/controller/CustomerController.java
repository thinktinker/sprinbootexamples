package com.sctp.fsd.jpa.controller;

import com.sctp.fsd.jpa.model.Customer;
import com.sctp.fsd.jpa.repository.CustomerRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
* Controller to manage incoming requests
* Using @Controller annotation
* */
@Controller
public class CustomerController {

    @Autowired  // Connect to CustomerRepository Interface (in another package)
    CustomerRepository customerRepository;

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

    @PostMapping("/add")public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) throws Exception{
        try {
            // try running the code block
            customerRepository.save(customer);
            return new ResponseEntity<>(customer, HttpStatus.CREATED);    }
        catch (Exception e) {
            // present the error(s) or return the error(s)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allCustomers() throws Exception{
        try{
            // try running the code block
            List<Customer> customers = (List<Customer>) customerRepository.findAll();

            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch (Exception e){
            // present the errors or return the error(s)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody Customer customer) throws Exception{

        try{
            // the exception here is invoked through a lambda expression ()->{}
            Customer currentCustomer = customerRepository.findById(id).orElseThrow(()->new Exception("Customer Not Found."));

            // update the customer
            currentCustomer.setFirstName(customer.getFirstName());
            currentCustomer.setLastName(customer.getLastName());
            currentCustomer.setEmail(customer.getEmail());
            currentCustomer.setPhone(customer.getPhone());
            Customer result = customerRepository.save(currentCustomer);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") Integer id) throws Exception{
        try{
            // We should only delete a customer, ONLY if the customer is found
            Customer customer = customerRepository.findById(id).orElseThrow(()->new Exception());
            customerRepository.delete(customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);

        }catch (Exception e){
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id) throws Exception{
        try{
            Customer customer = customerRepository.findById(id).orElseThrow(()->new Exception());
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getCustomerByEmailOrLastName(
            @RequestParam("email") String email,
            @RequestParam("lastName") String lastName
            ) throws Exception{
        try {
            List<Customer> customers = customerRepository.findByEmailContainingOrLastNameContaining(email, lastName);

            if(customers.isEmpty()){
                throw new Exception();
            }

            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
