package com.databasemapping.manytomany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data //Generates the getters, setters, toString, @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="sku")
    @NotBlank(message = "Product SKU cannot be blank.")
    @Size(min = 3, message = "Product SKU must be at least 3 characters.")
    @Size(max = 255, message = "Supplier SKU must not be more than 255 characters.")
    private String sku;

    @Column(name = "name")
    @NotBlank(message = "Product Name cannot be blank.")
    @Size(min = 3, message = "Product Name must be at least 3 characters.")
    @Size(max = 255, message = "Product Name must not be more than 255 characters.")
    private String name;

    @Column(name="date_added")
    private LocalDateTime dateAdded;

    @Column(name="date_updated")
    private LocalDateTime dateUpdated;

    @Lob
    @Column(name = "description")
    @NotBlank(message = "Product Desc. cannot be blank.")
    @Size(min = 3, message = "Product Desc. must be at least 3 characters.")
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product Price must be more than 0.")
    @Digits(integer = 10, fraction = 2, message = "Product Price must be numerical.")
    private BigDecimal price;

    @Column(name="quantity")
    @Min(value = 0, message = "Product Quantity must not be a negative number.")
    private int quantity;

    // many products belong to one supplier (e.g. product A, product B -> supplier X)
    // For the @ManyToOne example, Product class is the 'parent' that manages the Supplier class as a 'child'
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("name ASC")    // default order is ASC, changeable to DESC
    // @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    // @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Tag> tags;

    // add a tag to a product
    public void addTag(Tag tag){
        this.tags.add(tag);
        tag.getProducts().add(this);
    }

    // remove tag from a product
    public void removeTag(long id) {
        Tag tag = this.tags.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if (tag != null) {
            this.tags.remove(tag);
            tag.getProducts().remove(this); // helper function to remove association
        }
    }

    // TODO: Implement the statements and methods to store product image
    // @Lob
    // @Column(columnDefinition = "longlob")
    // private byte[] img;
    // private MultipartFile img;

}
