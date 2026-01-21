package com.hackerrank.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dto.PaginatedResponse;
import com.hackerrank.sample.dto.ProductRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.model.Product.Condition;
import com.hackerrank.sample.security.JwtAuthenticationFilter;
import com.hackerrank.sample.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ProductController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("UNIT-PC-01: Create product returns 201 Created")
    void createProduct_Success() throws Exception {
        final var request = new ProductRequest("Smart TV", new BigDecimal("499.99"), 5, Condition.NEW, List.of());
        final var response = new ProductResponse(1L, "Smart TV", new BigDecimal("499.99"), 5, Condition.NEW, List.of());

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Smart TV"));
    }

    @Test
    @DisplayName("UNIT-PC-02: Get by ID returns 200 OK")
    void getProductById_Success() throws Exception {
        final var response = new ProductResponse(10L, "Tablet", new BigDecimal("200.00"), 2, Condition.USED, List.of());

        when(productService.getProductById(10L)).thenReturn(response);

        mockMvc.perform(get("/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tablet"));
    }

    @Test
    @DisplayName("UNIT-PC-03: Update product returns 200 OK")
    void updateProduct_Success() throws Exception {
        final var request = new ProductRequest("Updated Phone", new BigDecimal("800"), 10, Condition.NEW, List.of());
        final var response = new ProductResponse(1L, "Updated Phone", new BigDecimal("800"), 10, Condition.NEW, List.of());

        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Phone"));
    }

    @Test
    @DisplayName("UNIT-PC-04: Delete product returns 204 No Content")
    void deleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    @DisplayName("UNIT-PC-05: Get all paginated returns 200 OK")
    void getAllProducts_Success() throws Exception {
        final var paginatedResponse = new PaginatedResponse<ProductResponse>(
                List.of(),
                0L,
                0,
                0,
                10
        );

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.pageNumber").value(0));
    }
}