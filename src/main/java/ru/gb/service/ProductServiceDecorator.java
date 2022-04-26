package ru.gb.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.gb.api.product.dto.ProductDto;
import ru.gb.entity.Product;
import ru.gb.web.dto.ProductManufacturerDto;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceDecorator implements ProductServiceInterface{
    private ProductService productService;

    public long count() {
        log.info("Started additional functionality");
        long result = productService.count();
        result = result * 10;
        return result;
    }

    public ProductDto save(final ProductDto productDto) {
        return productService.save(productDto);
    }


    public ProductDto findById(Long id) {
        return productService.findById(id);
    }


    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    public List<Product> findAllActive() {
        return productService.findAllActive();
    }

    public void deleteById(Long id) {
        productService.deleteById(id);
    }

    public void disable(Long id) {
        productService.disable(id);
    }

    public List<Product> findAll(int page, int size) {
        return productService.findAll(page, size);
    }

    public List<Product> findAllSortedById() {
        return productService.findAllSortedById();
    }

    public List<Product> findAllSortedById(int page, int size) {
        return productService.findAllSortedById(page, size);
    }


    public List<ProductManufacturerDto> findAllInfo() {
        return productService.findAllInfo();
    }
}