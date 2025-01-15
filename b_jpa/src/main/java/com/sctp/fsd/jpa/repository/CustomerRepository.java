package com.sctp.fsd.jpa.repository;

import com.sctp.fsd.jpa.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    // By extending this repository with CrudRepository
    // the methods from CrudRepository can be used by CustomerRepository

    // save()                - save and update operation
    // findById(Integer)     - find an instance by Id
    // findAll()             - find all instances of records
    // count()               - count all instances of records
    // delete(object)        - delete an instance
    // delete(Integer)       - delete an instance by Id
    // for all the methods, refer to CrudRepository interface

    // Create custom query
    List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);   // SELECT * from customer WHERE email LIKE "%email%" OR lastName LIKE "%lastName%";
}
