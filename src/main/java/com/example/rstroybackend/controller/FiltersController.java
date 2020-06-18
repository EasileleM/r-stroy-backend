package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.service.ProductService;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/filters")
public class FiltersController {
    private final ProductTypesService productTypesService;
    private final ProductService productService;

    @GetMapping("")
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
}