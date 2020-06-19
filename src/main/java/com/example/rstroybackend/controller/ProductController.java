package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.service.ProductService;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductTypesService productTypesService;

    @GetMapping("/api/v1/commons/products/filters")
    public ResponseEntity getFilters() {
        List<ProductType> types = productTypesService.findAll();
        Double maxPrice = Math.ceil(productService.getMaxPrice().doubleValue());
        Double minPrice = Math.floor(productService.getMinPrice().doubleValue());;

        Map<String, Object> result = new HashMap<>();

        result.put("types", types);
        result.put("lowestPrice", minPrice);
        result.put("highestPrice", maxPrice);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/commons/products")
    public ResponseEntity getFilteredProducts(
            @RequestParam(value="type", required=false) List<String> types,
            @RequestParam(value="search", required = false, defaultValue = "") String search,
            @RequestParam(value="maxPrice", required = false) Integer maxPrice,
            @RequestParam(value="minPrice", required = false) Integer minPrice,
            @RequestParam(value="id", required=false) List<Long> ids,
            @PageableDefault(size = 30, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Object result;
        if (ids == null) {
            result = productService.findByFilters(search, types, maxPrice, minPrice, pageable);
        } else {
            result = productService.findByIds(ids);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/commons/products/{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
        Product result = productService.findById(id);

        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/v1/admin/products")
    public ResponseEntity createProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.create(product);

        return ResponseEntity.ok(null);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/api/v1/admin/products")
    public ResponseEntity updateProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.update(product);

        return ResponseEntity.ok(null);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/v1/admin/products")
    public ResponseEntity deleteProduct(
            @Valid @RequestBody Product product
    ) {
        productService.delete(product.getId());

        return ResponseEntity.ok(null);
    }
}