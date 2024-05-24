package com.sctp.fsd.jpa.service;

import com.sctp.fsd.jpa.model.Customer;
import com.sctp.fsd.jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements CustomerServiceInterface {

    @Autowired
    final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> createCustomer(Customer customer) {
        try{
            // Try to insert a customer
            Customer newCustomer = customerRepository.save(customer);
            return Optional.of(newCustomer);
        }catch(Exception e){
            // Otherwise return empty object
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> updateCustomer(Integer id, Customer customer) {
        try{
            // Try to update a customer
            Optional<Customer> currentCustomer = customerRepository.findById(id);
            if(currentCustomer.isEmpty()){
                return Optional.empty();
            }
            Customer updateCustomer = currentCustomer.get();
            updateCustomer.setName(customer.getName());
            updateCustomer.setEmail(customer.getEmail());
            updateCustomer.setPhone(customer.getPhone());
            Customer updatedCustomer = customerRepository.save(updateCustomer);
            return Optional.of(updatedCustomer);
        }catch(Exception e){
            // Otherwise return empty object
            return Optional.empty();
        }
    }

    @Override
    public boolean removeCustomer(Integer id) {
        try{
            Optional<Customer> customer = customerRepository.findById(id);
            if(customer.isEmpty()){
                return false;
            }
            customerRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomer(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public long countCustomers() {
        long result = customerRepository.count();
        return result;
    }
}
