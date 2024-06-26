package com.databasemapping.manytomany.service;

import com.databasemapping.manytomany.dto.RequestResponse;
import com.databasemapping.manytomany.exception.PasswordBlankException;
import com.databasemapping.manytomany.model.Users;
import com.databasemapping.manytomany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    // signs up a user and stores the user to the Users table
    public RequestResponse signUp(RequestResponse registrationRequest){

        RequestResponse requestResponse = new RequestResponse();

        Users users = new Users();

        users.setName(registrationRequest.getName());

        users.setEmail(registrationRequest.getEmail());

        if(registrationRequest.getPassword().isBlank())
            throw new PasswordBlankException();

        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        users.setRole(registrationRequest.getRole());
        Users usersResult = userRepository.save(users);

        if (usersResult != null && usersResult.getId()>0) {
            requestResponse.setUsers(usersResult);
            requestResponse.setMessage("User saved successfully.");
        }

        return requestResponse;
    }

    // logs in the authenticated user
    public RequestResponse signIn(RequestResponse signinRequest){

        RequestResponse requestResponse = new RequestResponse();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));

        var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

        var jwt = jwtUtils.generateToken(user);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        requestResponse.setToken(jwt);
        requestResponse.setRefreshToken(refreshToken);
        requestResponse.setExpirationTime("24Hr");
        requestResponse.setMessage("Signed in successfully.");

        return requestResponse;
    }

    // refresh a token for an authenticated user
    public RequestResponse refreshToken(RequestResponse refreshTokenRequest){

        RequestResponse requestResponse = new RequestResponse();

        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

        Users users = userRepository.findByEmail(ourEmail).orElseThrow();

        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshTokenRequest.getToken());
            requestResponse.setExpirationTime("24Hr");
            requestResponse.setMessage("Refreshed token successfully.");
        }

        return requestResponse;
    }

    // returns the user information
    public RequestResponse profile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication); //prints the details of the user(name,email,password,roles e.t.c)
        System.out.println(authentication.getDetails()); // prints the remote ip
        System.out.println(authentication.getName()); //prints the EMAIL, the email was stored as the unique identifier

        var users = new Users();
        users = userRepository.findByEmail(authentication.getName()).orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setName(users.getName());
        requestResponse.setEmail(users.getEmail());
        requestResponse.setPassword(users.getPassword());

        return requestResponse;

    }

}
