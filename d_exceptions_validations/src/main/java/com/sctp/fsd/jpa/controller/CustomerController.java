package com.sctp.fsd.jpa.controller;

import com.sctp.fsd.jpa.exceptions.ResourceNotFoundException;
import com.sctp.fsd.jpa.model.Customer;
import com.sctp.fsd.jpa.service.CustomerService;
import com.sctp.fsd.jpa.service.CustomerServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerServiceInterface customerServiceInterface;

    // TODO:
    //  Implemented
    //  Add a constructor that injects the CustomerServiceInterface
    public CustomerController(CustomerServiceInterface customerServiceInterface) {
        this.customerServiceInterface = customerServiceInterface;
    }

    // TODO:
    //  Implemented
    //  Add a new customer
    @PostMapping("/add")
    public ResponseEntity<Object> addNewCustomer(@Valid @RequestBody Customer toAddCustomer) throws Exception{
        try{
            Optional<Customer> createdCustomer = customerServiceInterface.createCustomer(toAddCustomer);
            if(createdCustomer.isEmpty())
                throw new Exception("Unable to add customer.");
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO:
    //  Implemented
    //  Retrieve all customers
    @GetMapping("/all")
    public ResponseEntity<Object> getAllCustomers() throws Exception{
        try{
            List<Customer> customers = customerServiceInterface.getCustomers();
            if(customers.isEmpty()){
                throw new ResourceNotFoundException("all");
            }
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO:
    //  Implemented
    //  Update customer by id
    @PutMapping("{id}")
    public ResponseEntity<Object> updateCustomerById(@PathVariable("id") Integer id, @Valid @RequestBody Customer toUpdateCustomer) throws Exception{
        try{
            Optional<Customer> customer = customerServiceInterface.updateCustomer(id, toUpdateCustomer);
            if(customer.isEmpty()){
                throw new Exception("Customer not updated.");
            }
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Customer is not found.", HttpStatus.NOT_FOUND);
        }
    }

    // TODO:
    //  Implemented
    //  Delete customer by id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable("id") Integer id) throws Exception{
        try{
            boolean removed = customerServiceInterface.removeCustomer(id);
            if(!removed){
                throw new Exception("Customer is not found.");
            }
            return new ResponseEntity<>("Customer deleted.", HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // TODO:
    //  Implemented
    //  Retrieve a customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id) throws Exception{
        try{
            Optional<Customer> customer = customerServiceInterface.getCustomer(id);
            if(customer.isEmpty()){
                throw new ResourceNotFoundException(id.toString());
            }
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO
    //  Implemented
    //  Count all customers
    @GetMapping("/count")
    public ResponseEntity<Object> getCustomerCount() throws Exception{
        Long count = customerServiceInterface.countCustomers();
        return new ResponseEntity<>(count.intValue() , HttpStatus.OK);
    }
}
