package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;

public interface WarehouseService {
    AddressDto getWarehouseAddress(String warehouse);

    boolean newProductToWarehouse(NewProductInWarehouseRequest request, String warehouse);

    boolean addProductToWarehouse(AddProductToWarehouseRequest request, String warehouse);

    BookedProductsDto checkProductsAvailable(ShoppingCartDto cart, String warehouse);
}
