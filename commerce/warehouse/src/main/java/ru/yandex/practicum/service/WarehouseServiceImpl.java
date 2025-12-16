package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.WarehouseProductRepository;
import ru.yandex.practicum.dal.model.WarehouseProduct;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private static final Map<String, String> WH_ADDRESS = Map.of("WH_1", "ADDRESS_1", "WH_2", "ADDRESS_2");

    private final WarehouseProductRepository warehouseRepository;

    @Override
    public AddressDto getWarehouseAddress(String warehouse) {
        String wh_address = WH_ADDRESS.get(warehouse);

        return AddressDto.builder()
                .country(wh_address)
                .city(wh_address)
                .street(wh_address)
                .house(wh_address)
                .flat(wh_address)
                .build();
    }

    @Override
    public boolean newProductToWarehouse(NewProductInWarehouseRequest request, String warehouse) {
        warehouseRepository.findByWarehouseIdAndProductId(warehouse, request.getProductId())
                .ifPresent(e -> {throw new SpecifiedProductAlreadyInWarehouseException("Продукт id=" + request.getProductId() + " уже присутствует на складе id=" + warehouse);});

        warehouseRepository.save(WarehouseProduct.builder()
                .warehouseId(warehouse)
                .productId(request.getProductId())
                .fragile(request.getFragile())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .weight(request.getWeight())
                .quantity(0.0)
                .build());

        return true;
    }

    @Override
    public boolean addProductToWarehouse(AddProductToWarehouseRequest request, String warehouse) {
        WarehouseProduct product = warehouseRepository.findByWarehouseIdAndProductId(warehouse, request.getProductId()).orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Продукт id=" + request.getProductId() + " не найден на складе id=" + warehouse));
        product.setQuantity(request.getQuantity());
        warehouseRepository.save(product);

        return true;
    }

    @Override
    public BookedProductsDto checkProductsAvailable(ShoppingCartDto cart, String warehouse) {
        final Map<UUID, WarehouseProduct> productById = warehouseRepository.findByWarehouseIdAndProductIdIn(warehouse, cart.getProducts().keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        final BookedProductsDto bookingInfo = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        cart.getProducts().forEach((productId, quantity) -> {
            if (productById.containsKey(productId) && productById.get(productId).getQuantity() >= quantity) {
                WarehouseProduct product = productById.get(productId);
                bookingInfo.setDeliveryWeight(bookingInfo.getDeliveryWeight() + (product.getWeight() * quantity));
                bookingInfo.setDeliveryVolume(bookingInfo.getDeliveryVolume() + (product.getWidth() * product.getHeight() * product.getDepth() * quantity));
                bookingInfo.setFragile(bookingInfo.getFragile() || product.getFragile());
            } else {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточно продукта id=" + productId + " на складе id=" + warehouse);
            }
        });

        return bookingInfo;
    }
}
