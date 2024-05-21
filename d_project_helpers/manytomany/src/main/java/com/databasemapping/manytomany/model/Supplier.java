package com.databasemapping.manytomany.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //Generates the getters, setters, toString, @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="supplier", uniqueConstraints = {@UniqueConstraint(name ="email", columnNames = "email")})
@Builder

public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Supplier Name cannot be blank.")
    @Size(min = 3, message = "Supplier Name must be at least 3 characters.")
    @Size(max = 255, message = "Supplier Name must not be more than 255 characters.")
    private String name;

    @Column(name="address")
    @NotBlank(message = "Supplier Address cannot be blank.")
    @Size(min = 10, message = "Supplier Address must be at least 10 characters.")
    @Size(max = 255, message = "Supplier Address must not be more than 255 characters.")
    private String address;

    @Column(name="phone")
    @NotBlank(message = "Supplier Phone number cannot be blank.")
    @Pattern(regexp = "^\\d{8}$", message = "Supplier Phone number must be 8-digits only.")
    private String phone;

    @Column(name="email", unique = true)
    @NotBlank(message = "Supplier Email cannot be blank.")
    @Email(message = "Supplier Email is not valid.")
    private String email;

    @Column(name="date_added")
    private LocalDateTime dateAdded;

    @Column(name="date_updated")
    private LocalDateTime dateUpdated;
}
