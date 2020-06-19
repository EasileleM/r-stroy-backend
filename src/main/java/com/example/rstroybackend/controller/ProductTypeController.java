package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/api/v1/admin/productTypes")
public class ProductTypeController {
    private final ProductTypesService productTypesService;

    @GetMapping("")
    public ResponseEntity getAllProductTypes() {
        Set<ProductType> result = productTypesService.findAll();

        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    public ResponseEntity createProductType(
            @Valid @RequestBody ProductType productType
    ) {
        ProductType result = productTypesService.create(productType);

        return ResponseEntity.ok(null);
    }

    @PatchMapping("")
    public ResponseEntity updateProductType(
            @Valid @RequestBody ProductType productType
    ) {
        ProductType result = productTypesService.update(productType);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("")
    public ResponseEntity deleteProductType(
            @Valid @RequestBody ProductType productType
    ) {
        productTypesService.delete(productType.getId());

        return ResponseEntity.ok(null);
    }
}