package com.hackerrank.sample.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

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

    @NotBlank(message = "Seller name is mandatory")
    private String sellerName;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double sellerRating;

    @NotNull(message = "Shipping cost is mandatory")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal shippingCost;

    @NotBlank(message = "Delivery estimate is mandatory")
    private String estimatedDelivery;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<@NotBlank String> imageUrls = new ArrayList<>();

    public enum Condition {
        NEW, USED
    }
}