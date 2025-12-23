package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.OrderBookingRepository;
import ru.yandex.practicum.dal.WarehouseOrderRepository;
import ru.yandex.practicum.dal.WarehouseProductRepository;
import ru.yandex.practicum.dal.model.OrderBooking;
import ru.yandex.practicum.dal.model.WarehouseOrder;
import ru.yandex.practicum.dal.model.WarehouseProduct;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.OrderNotFound;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.ProductInShoppingCartNotInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private static final Map<String, String> WH_ADDRESS = Map.of("WH_1", "ADDRESS_1", "WH_2", "ADDRESS_2");

    private final WarehouseProductRepository warehouseRepository;
    private final WarehouseOrderRepository warehouseOrderRepository;
    private final OrderBookingRepository orderBookingRepository;

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
        return getBookedProductsInfo(warehouseRepository.findByWarehouseIdAndProductIdIn(warehouse, cart.getProducts().keySet()), cart.getProducts());
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest productsRequest, String warehouse) {

        final List<WarehouseProduct> warehouseProducts = warehouseRepository.findByWarehouseIdAndProductIdIn(warehouse, productsRequest.getProducts().keySet());

        final BookedProductsDto bookingInfo = getBookedProductsInfo(warehouseProducts, productsRequest.getProducts());

        createOrderBooking(productsRequest, warehouse);

        reduceProductQuantity(warehouseProducts, productsRequest.getProducts());

        return bookingInfo;
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest shippedOrder) {
        WarehouseOrder order = warehouseOrderRepository.findById(shippedOrder.getOrderId()).orElseThrow(() -> new OrderNotFound("Заказ id=" + shippedOrder.getOrderId() + " не найден!"));
        order.setDeliveryId(shippedOrder.getDeliveryId());
        warehouseOrderRepository.save(order);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products, String warehouse) {
        final Map<UUID, WarehouseProduct> productById = warehouseRepository.findByWarehouseIdAndProductIdIn(warehouse, products.keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        products.forEach((productId, quantity) -> {
            WarehouseProduct product = productById.get(productId);
            product.setQuantity(product.getQuantity() + quantity);
        });

        warehouseRepository.saveAll(productById.values());
    }

    private BookedProductsDto getBookedProductsInfo(List<WarehouseProduct> warehouseProducts, Map<UUID, Integer> orderedProducts) {
        final Map<UUID, WarehouseProduct> productById = warehouseProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        final BookedProductsDto bookingInfo = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        orderedProducts.forEach((productId, quantity) -> {
            if (!productById.containsKey(productId)) {
                throw new ProductInShoppingCartNotInWarehouse("Продукт id=" + productId + " не существует на складе");
            } else if (productById.get(productId).getQuantity() < quantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточно продукта id=" + productId + " на складе");
            } else {
                WarehouseProduct product = productById.get(productId);
                bookingInfo.setDeliveryWeight(bookingInfo.getDeliveryWeight() + (product.getWeight() * quantity));
                bookingInfo.setDeliveryVolume(bookingInfo.getDeliveryVolume() + (product.getWidth() * product.getHeight() * product.getDepth() * quantity));
                bookingInfo.setFragile(bookingInfo.getFragile() || product.getFragile());
            }
        });

        return bookingInfo;
    }

    private void createOrderBooking(AssemblyProductsForOrderRequest productsRequest, String warehouse) {
        final WarehouseOrder order = warehouseOrderRepository.save(WarehouseOrder.builder()
                        .orderId(productsRequest.getOrderId())
                        .warehouseId(warehouse)
                        .build());

        List<OrderBooking> orderBookings = productsRequest.getProducts().entrySet().stream()
                .map(p -> OrderBooking.builder()
                            .productId(p.getKey())
                            .orderId(order.getOrderId())
                            .quantity(p.getValue())
                            .build())
                .toList();

        orderBookingRepository.saveAll(orderBookings);
    }

    private void reduceProductQuantity(List<WarehouseProduct> warehouseProducts, Map<UUID, Integer> orderedProducts) {
        final Map<UUID, WarehouseProduct> productById = warehouseProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        orderedProducts.forEach((productId, quantity) -> {
                WarehouseProduct product = productById.get(productId);
                product.setQuantity(product.getQuantity() - quantity);
        });

        warehouseRepository.saveAll(productById.values());
    }
}
