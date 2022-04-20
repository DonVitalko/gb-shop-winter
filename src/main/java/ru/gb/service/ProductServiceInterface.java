package ru.gb.service;

import ru.gb.api.product.dto.ProductDto;
import ru.gb.entity.Product;
import ru.gb.web.dto.ProductManufacturerDto;

import java.util.List;

public interface ProductServiceInterface {
    public long count() ;
    public ProductDto save(final ProductDto productDto);
    public ProductDto findById(Long id);
    public List<ProductDto> findAll();
    public List<Product> findAllActive();
    public void deleteById(Long id);
    public void disable(Long id);
    public List<Product> findAll(int page, int size);
    public List<Product> findAllSortedById();
    public List<Product> findAllSortedById(int page, int size);
    public List<ProductManufacturerDto> findAllInfo();
}