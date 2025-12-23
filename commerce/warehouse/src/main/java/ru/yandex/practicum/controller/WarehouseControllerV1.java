package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.service.WarehouseService;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseControllerV1 {
    private static final String[] WAREHOUSES = new String[] {"WH_1", "WH_2"};
    private static final String CURRENT_WAREHOUSE = WAREHOUSES[Random.from(new SecureRandom()).nextInt(0,WAREHOUSES.length)];

    private final WarehouseService warehouseService;

    @PutMapping
    public boolean newProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request) {
        return warehouseService.newProductToWarehouse(request, CURRENT_WAREHOUSE);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductsAvailable(@RequestBody ShoppingCartDto cart) {
        return warehouseService.checkProductsAvailable(cart, CURRENT_WAREHOUSE);
    }

    @PostMapping("/add")
    public boolean addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        return warehouseService.addProductToWarehouse(request, CURRENT_WAREHOUSE);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress(CURRENT_WAREHOUSE);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest productsRequest) {
        return warehouseService.assemblyProductsForOrder(productsRequest, CURRENT_WAREHOUSE);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody ShippedToDeliveryRequest shippedOrder) {
        warehouseService.shippedToDelivery(shippedOrder);
    }

    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<UUID, Integer> products) {
        warehouseService.returnProducts(products, CURRENT_WAREHOUSE);
    }
}
