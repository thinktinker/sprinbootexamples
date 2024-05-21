package com.databasemapping.manytomany.repository;

import com.databasemapping.manytomany.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository <Tag, Long> {

    List<Tag> findTagsByProductsId(Long id);

    List<Tag> findByNameContaining(String name);
}
