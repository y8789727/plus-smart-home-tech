package ru.yandex.practicum.dto.warehouse;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProductsAvailable(@RequestBody ShoppingCartDto cart) throws FeignException;

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody AssemblyProductsForOrderRequest productsRequest) throws FeignException;

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress() throws FeignException;

    @PostMapping("/api/v1/warehouse/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest shippedOrder) throws FeignException;
}
