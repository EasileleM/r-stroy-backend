package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.enums.Status;
import com.example.rstroybackend.exceptions.InternalServerErrorException;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.repo.ProductTypeRepo;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class ProductTypesServiceImpl implements ProductTypesService {
    private final ProductTypeRepo productTypeRepo;

    @Override
    public ProductType findById(Long id) {
        ProductType result = productTypeRepo.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no productType found by id: {}", id);
        } else {
            log.info("IN findById - productType {} found by id: {}", result, id);
        }

        return result;
    }

    @Override
    public ProductType findByName(String name) {
        ProductType result = productTypeRepo.findByName(name).orElse(null);

        if (result == null) {
            log.warn("IN findByName - no productType found by name: {}", name);
        } else {
            log.info("IN findByName - productType {} found by name: {}", result, name);
        }

        return result;
    }

    @Override
    public Set<ProductType> findAll() {
        Set<ProductType> result = new HashSet<>(productTypeRepo.findAll());

        log.info("IN findAll - productsTypes: {} found", result);

        return result;
    }

    @Override
    public ProductType create(ProductType type) {
        type.setStatus(Status.ACTIVE);
        type.setCreated(new Date());
        type.setUpdated(new Date());

        ProductType createdProductType = productTypeRepo.save(type);

        if (createdProductType == null) {
            log.info("IN create - productType: {} creation failed", type);
            throw new InternalServerErrorException();
        }
        log.info("IN create - productType: {} successfully created", createdProductType);

        return createdProductType;
    }

    @Override
    public ProductType update(ProductType type) {
        ProductType targetProductType = productTypeRepo.findById(type.getId()).orElse(null);

        if (targetProductType == null) {
            log.info("IN update - ProductType: {} not found", type);
            throw new ResourceNotFoundException();
        }

        targetProductType.setName(type.getName());
        targetProductType.setUpdated(new Date());

        ProductType updatedProductType = productTypeRepo.save(type);

        if (updatedProductType == null) {
            log.info("IN update - ProductType: {} update failed", type);
            throw new InternalServerErrorException();
        }

        log.info("IN update - ProductType: {} successfully updated", updatedProductType);

        return updatedProductType;
    }

    @Override
    public void delete(Long id) {
        productTypeRepo.deleteById(id);

        log.info("IN delete - productType with id: {} successfully deleted", id);
    }
}
