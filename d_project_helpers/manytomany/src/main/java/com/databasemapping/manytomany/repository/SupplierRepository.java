package com.databasemapping.manytomany.repository;

import com.databasemapping.manytomany.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository <Supplier, Long> {

    List<Supplier> findByNameContaining(String name);
    @Query("select s from Supplier s where s.email LIKE %?1%")
    List<Supplier> findEmailLike(String email);
}
