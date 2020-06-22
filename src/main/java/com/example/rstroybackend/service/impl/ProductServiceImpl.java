package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.dto.JsonPage;
import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.ProductType;
import com.example.rstroybackend.enums.Status;
import com.example.rstroybackend.exceptions.BadRequestException;
import com.example.rstroybackend.exceptions.InternalServerErrorException;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.exceptions.ServiceUnavailableException;
import com.example.rstroybackend.repo.ProductRepo;
import com.example.rstroybackend.service.ProductService;
import com.example.rstroybackend.service.ProductTypesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    private final ProductTypesService productTypesService;

    @Override
    public JsonPage<Product> findByFilters(
            String search,
            Iterable<String> types,
            Integer maxPrice,
            Integer minPrice,
            Pageable pageable) {
        BigDecimal maxPriceBigDecimal = maxPrice == null ? null : new BigDecimal(maxPrice);
        BigDecimal minPriceBigDecimal = minPrice == null ? null : new BigDecimal(minPrice);

        JsonPage<Product> result = new JsonPage(productRepo.findByFilters(search, types, maxPriceBigDecimal, minPriceBigDecimal, pageable), pageable);

        if (result.getNumberOfElements() == 0) {
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
            throw new ResourceNotFoundException();
        }
        log.info("IN findById - product {} found by id: {}", result, id);

        return result;
    }

    @Override
    public Set<Product> findByIds(Iterable<Long> ids) {
        Set<Product> result = new HashSet<>(productRepo.findAllById(ids));

        if (result.isEmpty()) {
            log.info("IN findByIds - no products found by ids: {}", ids);
        } else {
            log.info("IN findByIds - products {} found by ids: {}", result, ids);
        }

        return result;
    }

    @Override
    public Product create(Product product) {
        product.setStatus(Status.ACTIVE);
        product.setCreated(new Date());
        product.setUpdated(new Date());

        Product createdProduct = productRepo.save(product);

        if (createdProduct == null) {
            log.info("IN create - product {}: creation failed", product);
            throw new InternalServerErrorException();
        }
        log.info("IN create - product: {} successfully created", createdProduct);

        return createdProduct;
    }

    @Override
    public Product update(Product product) {
        Long productId = product.getId();

        if (productId == null) {
            log.info("IN update - no id provided");
            throw new BadRequestException();
        }

        Product existedProduct = productRepo.findById(productId).orElse(null);

        if (existedProduct == null) {
            log.info("IN update - product: {} not found", product);
            throw new ResourceNotFoundException();
        }

        existedProduct.setAmount(product.getAmount());
        existedProduct.setDescription(product.getDescription());
        existedProduct.setImageURL(product.getImageURL());
        existedProduct.setName(product.getName());
        existedProduct.setTypes(product.getTypes());
        existedProduct.setUpdated(new Date());

        Product updatedProduct = productRepo.save(product);

        if (updatedProduct == null) {
            log.info("IN update - product: {} update failed", product);
            throw new InternalServerErrorException();
        }
        log.info("IN update - product: {} successfully updated", updatedProduct);

        return updatedProduct;
    }

    @Override
    public BigDecimal getMaxPrice() {
        BigDecimal maxPrice = productRepo.findByMaxPrice();

        if (maxPrice == null) {
            log.info("IN getMaxPrice - price not found");
            throw new ServiceUnavailableException();
        }

        log.info("IN getMaxPrice - price: {} found", maxPrice);

        return maxPrice;
    }

    @Override
    public BigDecimal getMinPrice() {
        BigDecimal minPrice = productRepo.findByMinPrice();

        if (minPrice == null) {
            log.info("IN getMinPrice - price not found");
            throw new ServiceUnavailableException();
        }

        log.info("IN getMinPrice - price: {} found", minPrice);

        return minPrice;
    }

    @Override
    public Map<String, Object> getFilters() {
        Set<ProductType> types = productTypesService.findAll();
        Double maxPrice = Math.ceil(getMaxPrice().doubleValue());
        Double minPrice = Math.floor(getMinPrice().doubleValue());;

        Map<String, Object> result = new HashMap<>();

        result.put("types", types);
        result.put("lowestPrice", minPrice);
        result.put("highestPrice", maxPrice);

        return result;
    }

    @Override
    public void delete(Product product) {
        Long productId = product.getId();
        if (productId == null) {
            log.info("IN delete - product with id: {} deletion failed", productId);
            throw new BadRequestException();
        }
        productRepo.deleteById(productId);

        log.info("IN delete - product with id: {} successfully deleted", productId);
    }
}
