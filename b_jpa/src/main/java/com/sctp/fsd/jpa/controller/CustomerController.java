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

@Controller
@RequestMapping("/jpa")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    // TODO:
    //  Implemented
    //  Add a new customer (Note: allow null values to be received first)
    @PostMapping(path="/add")
    public ResponseEntity<Object> addNewCustomer(@Nullable @RequestParam String name, @Nullable @RequestParam String email, @Nullable @RequestParam String phone) throws  Exception{
        try{
            Customer customer = new Customer(name, email, phone);
            customerRepository.save(customer);
            return new ResponseEntity<>("Customer added successfully.", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Unable to add customer.", HttpStatus.BAD_REQUEST);
        }

    }

    // TODO:
    //  Implemented
    //  Retrieve all customers
    @GetMapping("/all")
    public ResponseEntity<Object> getAllCustomers(){
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        if(customers.isEmpty()){
            return new ResponseEntity<>("No customer(s) found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // TODO:
    //  Implemented
    //  Update customer by id
    @PutMapping("{id}")
    public ResponseEntity<Object> updateCustomerById(@PathVariable("id") Integer id, @RequestBody Customer customer) throws Exception{
        try{
            Optional<Customer> currentCustomer = customerRepository.findById(id);
            Customer updateCustomer = currentCustomer.get();
            updateCustomer.setName(customer.getName());
            updateCustomer.setEmail(customer.getEmail());
            updateCustomer.setPhone(customer.getPhone());
            Customer result = customerRepository.save(updateCustomer);
            return new ResponseEntity<>(result, HttpStatus.OK);
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
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                customerRepository.deleteById(id);
                return new ResponseEntity<>("Customer deleted.", HttpStatus.OK);
            }
            throw new Exception("Customer is not found.");
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // TODO:
    //  Implemented
    //  Retrieve a customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id){
        Optional<Customer> customer = customerRepository.findById(id);
        if(!customer.isPresent())
            return new ResponseEntity<>("Customer is not found.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
