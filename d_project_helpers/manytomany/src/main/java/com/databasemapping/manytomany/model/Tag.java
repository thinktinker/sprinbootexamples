package com.databasemapping.manytomany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data //Generates the getters, setters, toString, @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag", uniqueConstraints = {@UniqueConstraint(name ="tag name", columnNames = "name")})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "Tag Name cannot be blank.")
    @Size(min = 3, message = "Tag Name must be at least 3 characters.")
    @Size(max = 255, message = "Tag Name must not be more than 255 characters.")
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "product_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private List<Product> products;

    // IMPORTANT:
    // The following shall be used if the intent
    // is to maintain that a Tag shall not be deleted
    // when it is used by a Product

    //    @ManyToMany(
    //            fetch = FetchType.LAZY,
    //            cascade = {
    //            CascadeType.PERSIST,
    //            CascadeType.MERGE
    //            },
    //            mappedBy = "tags"
    //    )
    //    @JsonIgnore
    //    private List<Product> products;

}
