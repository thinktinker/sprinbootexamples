package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Product;
import com.databasemapping.manytomany.model.Tag;
import com.databasemapping.manytomany.repository.ProductRepository;
import com.databasemapping.manytomany.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/public/api")
@CrossOrigin("*")
public class PublicController {

    // access to ProductRepository (aka dependency injection)
    @Autowired
    private ProductRepository productRepository;

    // access to TagRepository (aka dependency injection)
    @Autowired
    private TagRepository tagRepository;

    // get all products
    @GetMapping("/product")
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
    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable("id") Long id){

        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get all tags
    @GetMapping("/tag")
    public ResponseEntity<Object> getAllTags(@RequestParam(required = false) String name){

        List<Tag> result = new ArrayList<>();

        if(name == null){
            result.addAll(tagRepository.findAll());
        }else{
            result.addAll(tagRepository.findByNameContaining(name));
        }

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get all products by tag id
    @GetMapping("/tag/{id}/product")
    public ResponseEntity<Object> getProductsByTagID(@PathVariable("id") Long id){

        boolean tagExists = tagRepository.existsById(id);

        if(!tagExists){
            throw new ResourceNotFoundException();
        }

        List<Product> result = productRepository.findProductsByTagsId(id);

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
