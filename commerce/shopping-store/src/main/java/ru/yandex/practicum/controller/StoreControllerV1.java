package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductDto;
import ru.yandex.practicum.dto.product.QuantityState;
import ru.yandex.practicum.service.StoreService;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreControllerV1 {
    private final StoreService storeService;

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam("category")ProductCategory category, Pageable pageable) {
        return storeService.findProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return storeService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        return storeService.updateProduct(productDto.getProductId(), productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody String productId) {
        return storeService.removeProductFromStore(productId.replace("\"",""));
    }

    @PostMapping("/quantityState")
    public boolean setQuantityState(@RequestParam("productId") String productId,
                                    @RequestParam("quantityState") QuantityState quantityState) {
        return storeService.setQuantityState(SetProductQuantityStateRequest.builder()
                        .productId(productId)
                        .quantityState(quantityState)
                        .build());
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") String productId) {
        return storeService.findProductById(productId);
    }
}
