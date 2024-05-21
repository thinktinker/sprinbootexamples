package com.databasemapping.manytomany.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(name ="email", columnNames = "email")})

public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, message = "Name must be at least 3 characters.")
    @Size(max = 255, message = "Name must not be more than 255 characters.")
    private String name;

    @Column(name="email", unique = true)
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email is not valid.")
    private String email;

    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password is not strong.")
    // https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    //^                 # start-of-string
    //(?=.*[0-9])       # a digit must occur at least once
    //(?=.*[a-z])       # a lower case letter must occur at least once
    //(?=.*[A-Z])       # an upper case letter must occur at least once
    //(?=.*[@#$%^&+=])  # a special character must occur at least once
    //(?=\S+$)          # no whitespace allowed in the entire string
    //.{8,}             # anything, at least eight places though
    //$                 # end-of-string
    @Column(name="password")
    @NotBlank(message = "Password cannot be blank.")
    private String password;


    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role cannot be blank.")
    private EnumRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
