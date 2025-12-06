package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductDto;

public interface StoreService {
    Page<ProductDto> findProducts(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(String productId, ProductDto productDto);

    boolean removeProductFromStore(String productId);

    boolean setQuantityState(SetProductQuantityStateRequest quantityRequest);

    ProductDto findProductById(String productId);
}
