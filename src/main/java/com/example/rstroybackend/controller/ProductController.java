package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity getFilteredProducts( // TODO what if request empty
            @RequestParam(value="types", required=false, defaultValue = "") List<String> types,
            @RequestParam(value="search", required = false, defaultValue = "") String search
    ) {
        List<Product> result = productService.findByFilters(search, types);

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