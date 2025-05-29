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

    // find customer by email containing specific value(s)
    // using DERIVED queries
    List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);
    List<Customer> findByEmailContaining(String email);
    List<Customer> findByLastNameContaining(String lastName);
}
