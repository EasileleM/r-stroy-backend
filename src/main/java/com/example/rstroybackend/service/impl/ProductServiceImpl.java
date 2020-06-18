package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.Status;
import com.example.rstroybackend.repo.ProductRepo;
import com.example.rstroybackend.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    @Override
    public Page<Product> findByFilters(
            String search,
            List<String> types,
            Integer maxPrice,
            Integer minPrice,
            Pageable pageable) {
        Page<Product> result;

        BigDecimal maxPriceBigDecimal = maxPrice == null ? null : new BigDecimal(maxPrice);
        BigDecimal minPriceBigDecimal = minPrice == null ? null : new BigDecimal(minPrice);

        result = productRepo.findByFilters(search, types, maxPriceBigDecimal, minPriceBigDecimal, pageable);

        if (result.getSize() == 0) {
            log.warn("IN findByFilters - no products found by search: {} types: {} maxPrice: {} minPrice: {}", search, types, maxPrice, minPrice);
        } else {
            log.info("IN findByFilters - product {} found by search: {} types: {} maxPrice: {} minPrice: {}", result, search, types, maxPrice, minPrice);
        }

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
    public Integer getMaxPrice() {
        return productRepo.findByMaxPrice();
    }

    @Override
    public Integer getMinPrice() {
        return productRepo.findByMinPrice();
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);

        log.info("IN delete - product with id: {} successfully deleted", id);
    }
}
