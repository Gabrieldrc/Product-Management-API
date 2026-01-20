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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "Endpoints para la gestión de productos con cuotas de tráfico")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Error de validación o formato",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "Error de Título", value = "{\"status\": 400, \"message\": \"Validation failed: title: Title is mandatory and cannot be empty\", \"timestamp\": 1737368178000}"),
                                    @ExampleObject(name = "Error de Condición", value = "{\"status\": 400, \"message\": \"Invalid value for 'condition'. Allowed values are: [NEW, USED]\", \"timestamp\": 1737368178000}"),
                                    @ExampleObject(name = "Precio Negativo", value = "{\"status\": 400, \"message\": \"Validation failed: price: Price must be greater than zero\", \"timestamp\": 1737368178000}")
                            }))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest request) {
        return productService.createProduct(request);
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "400", description = "ID con formato incorrecto",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "ID No Numérico", value = "{\"status\": 400, \"message\": \"Invalid parameter: 'id' must be a numeric\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 999\", \"timestamp\": 1737368178000}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Actualizar producto", description = "Modifica un producto existente. Si el ID no existe, lanza 404. Valida el cuerpo de la petición.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "Validación de Campos", value = "{\"status\": 400, \"message\": \"Validation failed: price: Price must be greater than zero\", \"timestamp\": 1737368178000}"),
                                    @ExampleObject(name = "Formato de JSON", value = "{\"status\": 400, \"message\": \"Malformed JSON request or invalid field values\", \"timestamp\": 1737368178000}")
                            })),
            @ApiResponse(responseCode = "404", description = "El producto con el ID provisto no existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 99\", \"timestamp\": 1737368178000}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Eliminar producto", description = "Realiza una eliminación física del registro. Si el ID no existe, devuelve 404 para ser consistente con la búsqueda.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente (sin contenido en respuesta)"),
            @ApiResponse(responseCode = "400", description = "ID con formato inválido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "ID No Numérico", value = "{\"status\": 400, \"message\": \"Invalid parameter: 'id' must be a numeric\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "404", description = "No se puede eliminar un producto que no existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"message\": \"Product not found with id: 99\", \"timestamp\": 1737368178000}")))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @Operation(summary = "Listado paginado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada"),
            @ApiResponse(responseCode = "400", description = "Error en parámetros de búsqueda",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Campo Sort Inválido", value = "{\"status\": 400, \"message\": \"El campo de ordenamiento 'nonexistent' no existe\", \"timestamp\": 1737368178000}"))),
            @ApiResponse(responseCode = "429", description = "Rate Limit excedido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\": 429, \"message\": \"Too many requests. Rate limit exceeded.\", \"timestamp\": 1737368178000}")))
    })
    @GetMapping
    public PaginatedResponse<ProductResponse> getAllProducts(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return productService.getAllProducts(pageable);
    }
}