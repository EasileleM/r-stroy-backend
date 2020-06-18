package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity getFilteredProducts(
            @RequestParam(value="type", required=false) List<String> types,
            @RequestParam(value="search", required = false, defaultValue = "") String search,
            @RequestParam(value="maxPrice", required = false) Integer maxPrice,
            @RequestParam(value="minPrice", required = false) Integer minPrice,
            @RequestParam(value="id", required=false) List<Long> ids,
            @PageableDefault(size = 30, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info(search + " search result");
        Object result;
        if (ids == null) {
            result = productService.findByFilters(search, types, maxPrice, minPrice, pageable);
        } else {
            result = productService.findByIds(ids);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
        Product result = productService.findById(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping("") // TODO move it to admin controller then
    public ResponseEntity createProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.create(product);

        return ResponseEntity.ok(null);
    }

    @PutMapping("")
    public ResponseEntity updateProduct(
            @Valid @RequestBody Product product
    ) {
        Product result = productService.update(product);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("")
    public ResponseEntity deleteProduct(
            @Valid @RequestBody Product product
    ) {
        productService.delete(product.getId());

        return ResponseEntity.ok(null);
    }
}