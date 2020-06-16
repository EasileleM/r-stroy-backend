package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.Status;
import com.example.rstroybackend.repository.ProductRepo;
import com.example.rstroybackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    @Override
    public List<Product> findByFilters(String search, List<String> types) {
        List<Product> result = productRepo
                .findByNameContainingIgnoreCaseAndAndTypes_NameIn(search, types);

        return result;
    }

    @Override
    public Product findById(Long id) {
        Product result = productRepo.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no product found by id: {}", id);
        } else {
            log.info("IN findById - product {} found by id: {}", result, id);
        }

        return result;
    }

    @Override
    public List<Product> findByIds(List<Long> ids) {
        List<Product> result = productRepo.findAllById(ids);

        log.info("IN findByIds - products {} found by ids: {}", result, ids);

        return result;
    }

    @Override
    public Product create(Product product) {
        product.setStatus(Status.ACTIVE);
        product.setCreated(new Date());
        product.setUpdated(new Date());

        Product createdProduct = productRepo.save(product);

        log.info("IN create - product: {} successfully created", createdProduct);

        return createdProduct;
    }

    @Override
    public Product update(Product product) {
        Product existedProduct = productRepo.findById(product.getId()).orElse(null);

        existedProduct.setAmount(product.getAmount());
        existedProduct.setDescription(product.getDescription());
        existedProduct.setImageURL(product.getImageURL());
        existedProduct.setName(product.getName());
        existedProduct.setTypes(product.getTypes());
        existedProduct.setUpdated(new Date());

        Product updatedProduct = productRepo.save(product);

        log.info("IN update - product: {} successfully updated", updatedProduct);

        return updatedProduct;
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);

        log.info("IN delete - product with id: {} successfully deleted", id);
    }
}
