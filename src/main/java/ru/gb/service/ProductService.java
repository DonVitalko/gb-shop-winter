package ru.gb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.api.common.enums.Status;
import ru.gb.api.product.dto.ProductDto;
import ru.gb.dao.CategoryDao;
import ru.gb.dao.ManufacturerDao;
import ru.gb.dao.ProductDao;
import ru.gb.dao.identitymap.product.ProductIdentityMap;
import ru.gb.entity.Product;
import ru.gb.web.dto.ProductManufacturerDto;
import ru.gb.web.dto.mapper.ProductMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductServiceInterface{
    private final ProductDao productDao;
    private final ManufacturerDao manufacturerDao;
    private final CategoryDao categoryDao;
    private final ProductMapper productMapper;
    private final ProductIdentityMap productIdentityMap;

    @Transactional(propagation = Propagation.NEVER, isolation = Isolation.DEFAULT)
    public long count() {
        System.out.println(productDao.count());
        return productDao.count();
    }

    @Transactional
    public ProductDto save(final ProductDto productDto) {
        Product product = productMapper.toProduct(productDto, manufacturerDao, categoryDao);
        if (product.getId() != null) {
            productDao.findById(productDto.getId()).ifPresent(
                    (p) -> product.setVersion(p.getVersion())
            );
        }
        return productMapper.toProductDto(productDao.save(product));
    }


    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        ProductDto productDto = productIdentityMap.getProduct(id);

        if (productDto == null) {
            productDto = productMapper.toProductDto(productDao.findById(id).orElse(null));
            productIdentityMap.addProduct(productDto);
        }

        return productDto;
    }


    public List<ProductDto> findAll() {
        return productDao.findAll().stream().map(productMapper::toProductDto).collect(Collectors.toList());
    }

    public List<Product> findAllActive() {
        return productDao.findAllByStatus(Status.ACTIVE);
    }

    public void deleteById(Long id) {
        try {
            productIdentityMap.remove(id);
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void disable(Long id) {
        ProductDto productDto = productIdentityMap.getProduct(id);
        Product product;
        if (productDto == null){
            product = productDao.findById(id).orElse(null);
        } else {
            product = productMapper.toProduct(productDto, manufacturerDao, categoryDao);
        }
        if (product != null){
            product.setStatus(Status.DISABLED);
            productDao.save(product);
        }
    }

    public List<Product> findAll(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
    }

    public List<Product> findAllSortedById() {
        return productDao.findAllByStatus(Status.ACTIVE, Sort.by("id"));
    }

    public List<Product> findAllSortedById(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size, Sort.by("id")));
    }


    public List<ProductManufacturerDto> findAllInfo() {
        return productDao.findAll().stream().map(productMapper::toProductManufacturerDto).collect(Collectors.toList());
    }
}
