package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.ErrorResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "Endpoints for product management with rate limiting")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "Validation Error", value = "{\"status\": 400, \"message\": \"Validation failed: title: Title is mandatory\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest request) {
        return productService.createProduct(request);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "Invalid ID", value = "{\"status\": 400, \"message\": \"Invalid parameter: 'id' must be a numeric\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 999\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Update product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 400, \"message\": \"Validation failed: price: Price must be greater than zero\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 99\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 99\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @Operation(summary = "Get paginated products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "Invalid Sort", value = "{\"status\": 400, \"message\": \"Sorting field 'invalid' does not exist\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @GetMapping
    public PaginatedResponse<ProductResponse> getAllProducts(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return productService.getAllProducts(pageable);
    }
}