package ru.gb.dao.identitymap.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;
import ru.gb.api.product.dto.ProductDto;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@NoArgsConstructor
@Getter
public class ProductIdentityMap {
    private Map<Long, ProductDto> map = new LinkedHashMap<>();
    private int limit = 10;

    public void addProduct(ProductDto productDto){
        if (map.size() > 10) {
            Map.Entry<Long, ProductDto> entry = map.entrySet().iterator().next();
            map.remove(entry.getKey());
        }

        map.put(productDto.getId(), productDto);
    }

    public ProductDto getProduct(Long id){
        return map.get(id);
    }

    public void remove(Long id){
        map.remove(id);
    }
}