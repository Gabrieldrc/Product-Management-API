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

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 12, fraction = 2)
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Stock is mandatory")
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "Condition is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<@NotBlank String> imageUrls = new ArrayList<>();

    public enum Condition {
        NEW, USED
    }

    public Product() {
    }

    public Product(
            final String title,
            final BigDecimal price,
            final Integer stock,
            final Condition condition,
            final List<String> imageUrls
    ) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.condition = condition;
        this.imageUrls = imageUrls;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(final Integer stock) {
        this.stock = stock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(final Condition condition) {
        this.condition = condition;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(final List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}