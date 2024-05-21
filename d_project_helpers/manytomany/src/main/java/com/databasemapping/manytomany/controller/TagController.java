package com.databasemapping.manytomany.controller;

import com.databasemapping.manytomany.exception.ResourceNotFoundException;
import com.databasemapping.manytomany.model.Product;
import com.databasemapping.manytomany.model.Tag;
import com.databasemapping.manytomany.repository.ProductRepository;
import com.databasemapping.manytomany.repository.TagRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/tag")
@CrossOrigin("*")
public class TagController {

    // access to ProductRepository (aka dependency injection)
    @Autowired
    private ProductRepository productRepository;

    // access to TagRepository (aka dependency injection)
    @Autowired
    private TagRepository tagRepository;

    // get all tags
    @GetMapping("")
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

    // get a tag by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTag(@PathVariable("id") Long id){

        Tag result = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get all tags of a product by id
    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getTagsByProductId(@PathVariable("id") Long id){

        boolean productExists = productRepository.existsById(id);

        if(!productExists){
            throw new ResourceNotFoundException();
        }

        List<Tag> result = tagRepository.findTagsByProductsId(id);

        if(result.isEmpty()){
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // get all products by tag id
    @GetMapping("/{id}/product")
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

    // save tag(s)
    @PostMapping("/save")
    public ResponseEntity<Object> saveTags(@RequestBody List<Tag> tags){

        List<Tag> result = tagRepository.saveAll(tags);

        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // save OR update tag(s) to a product
    @RequestMapping(value = {"/save/product/{id}", "/update/product/{id}"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> saveTagsByProductId(@PathVariable("id") Long id, @RequestBody List<Tag> tags){

        List<Tag> newTagList = new ArrayList<>();

        // discontinue if any of the tag(s) is invalid - else, add to newList
        for(Tag tag : tags){
            Tag newTag = tagRepository.findById(tag.getId()).orElseThrow(() -> new ResourceNotFoundException("Tag id " + tag.getId() + " is not found."));
            newTagList.add(newTag);
        }

        // find the affected Product
        Product product = productRepository.findById(id).map(_product -> {

            // important: store current tags from product (by VALUE)
            List<Tag> currentList = new ArrayList<>(_product.getTags());

            // add each tag in the newList to product, if not found in currentList
            for(Tag newTag : newTagList){
                if(!currentList.contains(newTag)){
                    _product.getTags().add(newTag);
                }
            }

            // remove each tag from product, if it is not found in the new List
            for(Tag currentTag : currentList){
                if(!newTagList.contains(currentTag))
                    _product.getTags().remove(currentTag);
            }

            // save the product
            return productRepository.save(_product);

        }).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    // remove tag
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable("id") Long id){

        Tag result = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

        tagRepository.deleteById(id);

        boolean tagExists = tagRepository.existsById(id);

        if(tagExists){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // remove MULTIPLE tag(s)
    @DeleteMapping("")
    public ResponseEntity<Object> deleteTags(@RequestBody List<Tag> tags){

        List<Tag> deleteList = new ArrayList<>();

        // discontinue if any of the tag(s) is invalid - else, add to newList
        for(Tag tag : tags){
            Tag newTag = tagRepository.findById(tag.getId()).orElseThrow(() -> new ResourceNotFoundException());
            deleteList.add(newTag);
        }

        for(Tag tag : deleteList){
            tagRepository.deleteById(tag.getId());
        }

        return new ResponseEntity<>(deleteList, HttpStatus.OK);

    }

    // remove tag(s) from a product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteTagsByProductId(@PathVariable("id") Long id, @RequestBody List<Tag> tags){

        List<Tag> newTagList = new ArrayList<>();

        // discontinue if any of the tag(s) is invalid - else, add to newList
        for(Tag tag : tags){
            Tag newTag = tagRepository.findById(tag.getId()).orElseThrow(() -> new ResourceNotFoundException());
            newTagList.add(newTag);
        }

        // find the affected Product and delete the tags from newList
        Product result = productRepository.findById(id).map(_product ->{

            for(Tag tag : newTagList){
                _product.removeTag(tag.getId());
            }

            return productRepository.save(_product);
        }).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
