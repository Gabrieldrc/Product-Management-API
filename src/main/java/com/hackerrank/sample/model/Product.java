package com.hackerrank.sample.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory and cannot be empty")
    @Size(max = 100, message = "Title must not exceed 100 characters") // Sincronizado con DTO
    @Column(length = 100) // Indica a JPA/H2 que el límite físico es 100
    private String title;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 12, fraction = 2, message = "Price format must be up to 12 digits and 2 decimals")
    private BigDecimal price;

    @NotNull(message = "Stock is mandatory")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Condition is mandatory (NEW or USED)")
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls = new ArrayList<>();

    public enum Condition {
        NEW, USED
    }

    public Product() {
    }

    public Product(String title, BigDecimal price, Integer stock, Condition condition, List<String> imageUrls) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.condition = condition;
        this.imageUrls = imageUrls;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}