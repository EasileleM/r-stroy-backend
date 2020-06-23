package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/api/v1/admin/productTypes")
public class ProductTypeControllerV1 {
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

        return ResponseEntity.ok(result);
    }

    @PatchMapping("")
    public ResponseEntity updateProductType(
            @Valid @RequestBody ProductType productType
    ) {
        ProductType result = productTypesService.update(productType);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("")
    public ResponseEntity deleteProductType(
            @Valid @RequestBody ProductType productType
    ) {
        productTypesService.delete(productType.getId());

        return ResponseEntity.ok(null);
    }
}