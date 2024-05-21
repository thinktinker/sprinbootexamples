package com.databasemapping.manytomany.repository;

import com.databasemapping.manytomany.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository <Product, Long> {

    List<Product> findProductsByTagsId(Long id);

    List<Product> findByNameContaining(String name);

    @Query("select p from Product p where p.name LIKE %?1%")
    List<Product> findByNameLike(String name);
}
