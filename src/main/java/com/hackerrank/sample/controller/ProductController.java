package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "Endpoints for product management")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product and persists it in the database.
     *
     * @param request Validated product data.
     * @return Created product details.
     */
    @Operation(summary = "Create a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Validation Error", value = """
                                    {
                                        "title": "Validation Error",
                                        "status": 400,
                                        "detail": "Validation failed: title: Title is mandatory",
                                        "timestamp": "2026-01-21T15:00:00Z"
                                    }
                                    """)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid final ProductRequest request) {
        return productService.createProduct(request);
    }

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id Product ID.
     * @return Product details.
     */
    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable final Long id) {
        return productService.getProductById(id);
    }

    /**
     * Updates an existing product's information.
     */
    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable final Long id,
            @RequestBody @Valid final ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    /**
     * Removes a product from the system.
     */
    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable final Long id) {
        productService.deleteProductById(id);
    }

    /**
     * Retrieves a paginated list of products with sorting capabilities.
     *
     * @param pageable Pagination and sorting parameters.
     * @return Paginated response containing product data.
     */
    @Operation(summary = "Get paginated products")
    @GetMapping
    public PaginatedResponse<ProductResponse> getAllProducts(
            @ParameterObject @PageableDefault(size = 10, sort = "id") final Pageable pageable) {
        return productService.getAllProducts(pageable);
    }
}