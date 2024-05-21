package com.sctp.fsd.jpa.repository;

import com.sctp.fsd.jpa.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // By extending this Repository with CrudRepository, these default methods are implemented
    // save()
    // findOne()
    // findById()
    // findAll()
    // count()
    // delete()
    // deleteById()
}
