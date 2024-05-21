package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Product;
import com.databasemapping.manytomany.model.Supplier;
import com.databasemapping.manytomany.repository.ProductRepository;
import com.databasemapping.manytomany.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin/api/product")
@CrossOrigin("*")
public class ProductController {

    // access to ProductRepository (aka dependency injection)
    @Autowired
    private ProductRepository productRepository;

    // access to SupplierRepository (aka dependency injection)
    @Autowired
    private SupplierRepository supplierRepository;

    // get all products
    @GetMapping("")
    public ResponseEntity<Object> getAllProducts(@RequestParam(required = false) String name){

        List<Product> result = new ArrayList<>();

        if(name == null){
            result.addAll(productRepository.findAll());
        }else{
            result.addAll(productRepository.findByNameContaining(name));
        }

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get a product by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable("id") Long id){

        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // save product by supplier id
    @PostMapping("/save/supplier/{id}")
    public ResponseEntity<Object> saveProductBySupplierId(@PathVariable("id") Long id, @Valid @RequestBody Product product){

        Product result = supplierRepository.findById(id).map(_supplier ->{

            LocalDateTime timeNow = LocalDateTime.now();
            product.setDateAdded(timeNow);
            product.setDateUpdated(timeNow);

            // With manytomany (Product -> Supplier)
            // set an existing supplier to the product
            // which also means the supplier can be changed later (update)
            product.setSupplier(_supplier);
            return  productRepository.save(product);

        }).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // update product by id
    // alternatively, update product by id and by supplier id
    @PutMapping(value = {"/{id}", "/{id}/supplier/{supplierid}"})
    public ResponseEntity<Object> updateProductAndSupplier(@PathVariable(required = true, name = "id") Long id, @PathVariable(required = false, name = "supplierid") Long supplierid, @Valid @RequestBody Product product) throws Exception{

        try {

            Product result = productRepository.findById(id).map(_product -> {

                _product.setSku(product.getSku());
                _product.setName(product.getName());
                _product.setDescription(product.getDescription());
                _product.setPrice(product.getPrice());
                _product.setDateUpdated(LocalDateTime.now());
                _product.setQuantity(product.getQuantity());

                if (supplierid != null) {
                    Supplier byId = supplierRepository.findById(supplierid).get();
                    _product.setSupplier(byId);
                }
                return productRepository.save(_product);

            }).orElseThrow(() -> new ResourceNotFoundException());

            return ResponseEntity.status(HttpStatus.OK).body(result);

        }catch (NoSuchElementException ex){
            throw new ResourceNotFoundException("Resource not found. Unable to update product.");
        }
    }

    // delete product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id){

        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        productRepository.deleteById(id);

        boolean productExists = productRepository.existsById(id);

        if(productExists){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // get products by keyword
    @GetMapping("/findbyname")
    public ResponseEntity<Object> getProductsByNameLike(@RequestParam String name){

        if(name.isEmpty()){
            throw new ResourceNotFoundException();
        }

        List<Product> result = productRepository.findByNameLike(name);

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
