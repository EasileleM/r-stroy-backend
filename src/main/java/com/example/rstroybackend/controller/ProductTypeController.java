package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/productTypes")
public class ProductTypeController {
    private final ProductTypesService productTypesService;

    @GetMapping("")
    public ResponseEntity getAllProductTypes() {
        List<ProductType> result = productTypesService.findAll();

        return ResponseEntity.ok(result);
    }

    @PostMapping("") // TODO move it to admin controller then
    public ResponseEntity createProductType(
            @Valid @RequestBody ProductType productType
    ) {
        ProductType result = productTypesService.create(productType);

        return ResponseEntity.ok(null);
    }

    @PutMapping("")
    public ResponseEntity updateProductType(
            @Valid @RequestBody ProductType productType
    ) {
        ProductType result = productTypesService.update(productType);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("") // TODO make delete enabled disabled things work
    public ResponseEntity deleteProductType(
            @Valid @RequestBody ProductType productType
    ) {
        productTypesService.delete(productType.getId());

        return ResponseEntity.ok(null);
    }
}