package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.ProductRepository;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductDto;
import ru.yandex.practicum.dto.product.ProductState;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.dal.model.Product;
import ru.yandex.practicum.dal.model.mapper.ProductMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductDto> findProducts(ProductCategory category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable).map(ProductMapper::mapToProductDto);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productRepository.save(Product.builder()
                .id(UUID.randomUUID())
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .category(productDto.getProductCategory())
                .imageSrc(productDto.getImageSrc())
                .price(productDto.getPrice())
                .build());
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductDto productDto) {
        Product product = getProductById(productId);

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setCategory(productDto.getProductCategory());
        product.setPrice(productDto.getPrice());

        return ProductMapper.mapToProductDto(productRepository.save(product));
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = getProductById(productId);
        if (ProductState.DEACTIVATE.equals(product.getProductState())) {
            return false;
        }

        product.setProductState(ProductState.DEACTIVATE);

        productRepository.save(product);

        return true;
    }

    @Override
    public boolean setQuantityState(SetProductQuantityStateRequest quantityRequest) {
        Product product = getProductById(quantityRequest.getProductId());

        product.setQuantityState(quantityRequest.getQuantityState());

        productRepository.save(product);

        return true;

    }

    @Override
    public ProductDto findProductById(UUID productId) {
        return ProductMapper.mapToProductDto(getProductById(productId));
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product id=" + productId + " not found"));
    }
}
