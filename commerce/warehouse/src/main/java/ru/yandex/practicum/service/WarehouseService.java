package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    AddressDto getWarehouseAddress(String warehouse);

    boolean newProductToWarehouse(NewProductInWarehouseRequest request, String warehouse);

    boolean addProductToWarehouse(AddProductToWarehouseRequest request, String warehouse);

    BookedProductsDto checkProductsAvailable(ShoppingCartDto cart, String warehouse);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest productsRequest, String warehouse);

    void shippedToDelivery(ShippedToDeliveryRequest shippedOrder);

    void returnProducts(Map<UUID, Integer> products, String warehouse);
}
