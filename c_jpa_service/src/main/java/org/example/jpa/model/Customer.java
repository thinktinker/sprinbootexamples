package org.example.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder // provides a clean and readable way to create objects.
@Data //Generates the getters, setters, toString, @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "ID must be a positive number")
    private Long id;

    @Column (nullable = false)
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, message = "Name must be at least 3 characters.")
    @Size(max = 255, message = "Name must not be more than 255 characters.")
    private String name;

    @Column (nullable = false, unique = true)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email is not valid.")
    private String email;

    @Column
    @Pattern(regexp = "^\\d{8}$", message = "Phone number must be 8-digits only.")
    private String phone;

}
