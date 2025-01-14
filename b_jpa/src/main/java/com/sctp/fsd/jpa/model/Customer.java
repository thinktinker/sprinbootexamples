package com.sctp.fsd.jpa.model;

import jakarta.persistence.*;

@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="name", nullable = false)  // TODO :  Implemented - name cannot be null
    String name;

    @Column(name="email", nullable = false) // TODO :  Implemented - email cannot be null
    String email;
    @Column(name="phone")
    String phone;


    // TODO :
    //  Implemented
    //  the empty constructor is required
    //  when creating empty instances of Customer (e.g. getAllCustomers)
    public Customer(){}

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
