package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.OrderProductsRepository;
import ru.yandex.practicum.dal.OrdersRepository;
import ru.yandex.practicum.dal.model.Order;
import ru.yandex.practicum.dal.model.OrderProduct;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderMapper;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.dto.delivery.DeliveryClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.dto.payment.PaymentClient;
import ru.yandex.practicum.dto.warehouse.WarehouseClient;
import ru.yandex.practicum.exception.NoOrderFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;

    @Override
    public OrderDto createOrder(CreateNewOrderRequest newOrder, String userId) {
        final Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .state(OrderState.NEW)
                .cartId(newOrder.getShoppingCart().getShoppingCartId())
                .userId(userId)
                .products(new HashMap<>())
                .build();

        newOrder.getShoppingCart().getProducts().forEach((key, value) ->
                            order.getProducts().put(key, OrderProduct.builder()
                                            .orderId(order.getOrderId())
                                            .productId(key)
                                            .quantity(value)
                                            .build()));

        DeliveryDto delivery = deliveryClient.planDelivery(DeliveryDto.builder()
                        .orderId(order.getOrderId())
                        .toAddress(newOrder.getDeliveryAddress())
                        .fromAddress(warehouseClient.getWarehouseAddress())
                        .build());
        order.setDeliveryId(delivery.getDeliveryId());

        order.setProductsPrice(paymentClient.productCost(OrderMapper.mapToDto(order)));

        saveOrderAndProducts(order);

        return OrderMapper.mapToDto(getOrderById(order.getOrderId()));
    }

    @Override
    public List<OrderDto> findUserOrders(String userName) {
        final Map<UUID, Order> ordersById = ordersRepository.findByUserId(userName).stream()
                .collect(Collectors.toMap(Order::getOrderId, Function.identity()));

        orderProductsRepository.findByOrderIdIn(ordersById.keySet()).forEach(e -> {
                    Order o = ordersById.get(e.getOrderId());
                    if (o.getProducts() == null) {
                        o.setProducts(new HashMap<>());
                    }
                    o.getProducts().put(e.getProductId(), e);
                });

        return ordersById.values().stream().map(OrderMapper::mapToDto).toList();
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest returnRequest) {
        final Order order = getOrderById(returnRequest.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        returnRequest.getProducts().forEach((productId, quantity) -> {
            OrderProduct orderProduct = order.getProducts().get(productId);
            orderProduct.setQuantity(orderProduct.getQuantity() - quantity);
        });

        saveOrderAndProducts(order);

        return OrderMapper.mapToDto(getOrderById(order.getOrderId()));
    }

    @Override
    public OrderDto setOrderState(UUID orderId, OrderState state) {
        final Order order = getOrderById(orderId);
        order.setState(state);
        ordersRepository.save(order);
        return OrderMapper.mapToDto(getOrderById(order.getOrderId()));
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        final Order order = getOrderById(orderId);
        order.setTotalPrice(paymentClient.getTotalCost(OrderMapper.mapToDto(order)));
        ordersRepository.save(order);
        return OrderMapper.mapToDto(getOrderById(order.getOrderId()));
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        final Order order = getOrderById(orderId);
        order.setDeliveryPrice(deliveryClient.deliveryCost(OrderMapper.mapToDto(order)));
        ordersRepository.save(order);
        return OrderMapper.mapToDto(getOrderById(order.getOrderId()));
    }

    private Order getOrderById(UUID orderId) {
        Order order = ordersRepository.findById(orderId).orElseThrow(() -> new NoOrderFoundException(String.format("Не найдена заказ id=%s", orderId)));
        order.setProducts(orderProductsRepository.getProductsByOrderId(order.getOrderId()).stream()
                .collect(Collectors.toMap(OrderProduct::getProductId, Function.identity())));
        return order;
    }

    private void saveOrderAndProducts(Order order) {
        ordersRepository.save(order);
        orderProductsRepository.saveAll(order.getProducts().values());
    }
}
