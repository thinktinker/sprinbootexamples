package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Supplier;
import com.databasemapping.manytomany.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api/supplier")
@CrossOrigin("*")
public class SupplierController {

    // access to SupplierRepository (aka dependency injection)
    @Autowired
    private SupplierRepository supplierRepository;

    // get all suppliers
    @GetMapping("")
    public ResponseEntity<Object> getAllSuppliers(@RequestParam(required = false) String name){

        List<Supplier> result = new ArrayList<>();

        if(name == null){
            result.addAll(supplierRepository.findAll());
        }else{
            result.addAll(supplierRepository.findByNameContaining(name));
        }

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get supplier by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getSupplier(@PathVariable("id") Long id){

        Supplier result = supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // save supplier
    @PostMapping("/save")
    public ResponseEntity<Object> saveSupplier(@Valid @RequestBody Supplier supplier){

        LocalDateTime timeNow = LocalDateTime.now();
        supplier.setDateAdded(timeNow);
        supplier.setDateUpdated(timeNow);

        Supplier result = supplierRepository.save(supplier);

        boolean supplierExists = supplierRepository.existsById(result.getId());

        if(!supplierExists){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // update supplier by id
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSupplier(@PathVariable("id") Long id, @Valid @RequestBody Supplier supplier){

        Supplier result = supplierRepository.findById(id).map(_supplier ->{

            _supplier.setName(supplier.getName());
            _supplier.setAddress(supplier.getAddress());
            _supplier.setPhone(supplier.getPhone());
            _supplier.setEmail(supplier.getEmail());
            _supplier.setDateUpdated(LocalDateTime.now());

            return supplierRepository.save(_supplier);
        }).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // delete supplier by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSupplier(@PathVariable("id") Long id){

        Supplier result = supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        supplierRepository.deleteById(id);

        boolean supplierExists = supplierRepository.existsById(id);

        if(supplierExists){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // get all suppliers by email
    @GetMapping("/findbyemail")
    public ResponseEntity<Object> getSuppliersByEmailLike(@RequestParam String email){

        if(email.isEmpty()){
            throw new ResourceNotFoundException();
        }

        List<Supplier> result = supplierRepository.findEmailLike(email);

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
