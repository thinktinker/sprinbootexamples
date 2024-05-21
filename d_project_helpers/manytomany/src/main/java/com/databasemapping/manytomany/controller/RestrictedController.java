package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.dto.RequestResponse;
import com.databasemapping.manytomany.model.Users;
import com.databasemapping.manytomany.repository.UserRepository;
import com.databasemapping.manytomany.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
public class RestrictedController {

    @Autowired
    private AuthService authService;

    // restricted view for user only
    @GetMapping("/user/view")
    public ResponseEntity<Object> userView(){
        // TODO: Implement the statements to return user view data
        return ResponseEntity.status(HttpStatus.OK).body("Only authenticated User can access this endpoint.");
    }

    // restricted view for administrator only
    @GetMapping("/admin/view")
    public ResponseEntity<Object> adminView(){
        // TODO: Implement the statements to return admin view data
        return ResponseEntity.status(HttpStatus.OK).body("Only authenticated Administrator can access this endpoint.");
    }

    // restricted view for user or administrator to update profile info.
    @PostMapping("/restricted/updateprofile")
    public ResponseEntity<Object> updateProfile(){
        // TODO: Implement the statements to allow the user to update the profile
        return null;
    }

    // restricted view for user or view profile info.
    @GetMapping("/restricted/profile")
    public ResponseEntity<Object> profileView(){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.profile());
    }

}
