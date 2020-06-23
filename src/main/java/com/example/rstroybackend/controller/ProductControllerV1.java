package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ProductControllerV1 {
    private final ProductService productService;

    @GetMapping("/commons/products/filters")
    public ResponseEntity getFilters() {
        Map<String, Object> result = productService.getFilters();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/commons/products")
    public ResponseEntity getProducts(
            @RequestParam(value="type", required=false) Set<String> types,
            @RequestParam(value="search", required = false) String search,
            @RequestParam(value="maxPrice", required = false) Integer maxPrice,
            @RequestParam(value="minPrice", required = false) Integer minPrice,
            @RequestParam(value="id", required=false) Set<Long> ids,
            @PageableDefault(size = 30, sort = { "created" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (ids == null) {
            return ResponseEntity.ok(productService.findByFilters(search, types, maxPrice, minPrice, pageable));
        }
        return ResponseEntity.ok(productService.findByIds(ids));
    }

    @GetMapping("/commons/products/{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
        Product result = productService.findById(id);

        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/products")
    public ResponseEntity createProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.create(product);

        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/admin/products")
    public ResponseEntity updateProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.update(product);

        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/products")
    public ResponseEntity deleteProduct(
            @Valid @RequestBody Product product
    ) {
        productService.delete(product);

        return ResponseEntity.ok(null);
    }
}