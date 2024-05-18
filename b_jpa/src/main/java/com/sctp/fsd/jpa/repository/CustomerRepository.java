package com.sctp.fsd.jpa.repository;

import com.sctp.fsd.jpa.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    // By extending this Repository with CrudRepository, these default methods are implemented
    // save()
    // findOne()
    // findById()
    // findAll()
    // count()
    // delete()
    // deleteById()
}
