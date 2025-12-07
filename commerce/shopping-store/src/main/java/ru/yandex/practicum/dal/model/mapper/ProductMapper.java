package ru.yandex.practicum.dal.model.mapper;

import ru.yandex.practicum.dto.product.ProductDto;
import ru.yandex.practicum.dal.model.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getCategory())
                .price(product.getPrice())
                .build();
    }
}
