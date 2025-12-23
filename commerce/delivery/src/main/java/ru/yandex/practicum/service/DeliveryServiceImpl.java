package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.DeliveryRepository;
import ru.yandex.practicum.dal.model.Delivery;
import ru.yandex.practicum.dto.DeliveryMapper;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.order.OrderClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.dto.warehouse.WarehouseClient;
import ru.yandex.practicum.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private static final Double DELIVERY_BASE_COST = 5.0;

    private final DeliveryRepository deliveryRepository;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return DeliveryMapper.mapToDto(deliveryRepository.save(Delivery.builder()
                        .deliveryId(UUID.randomUUID())
                        .orderId(deliveryDto.getOrderId())
                        .state(DeliveryState.CREATED)
                        .fromAddress(deliveryDto.getFromAddress())
                        .toAddress(deliveryDto.getToAddress())
                        .build()));
    }

    @Override
    public void pickDelivery(UUID deliveryId) {
        setDeliveryState(deliveryId, DeliveryState.IN_PROGRESS);

        Delivery delivery = getDeliveryById(deliveryId);

        warehouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                        .deliveryId(delivery.getDeliveryId())
                        .orderId(delivery.getOrderId())
                        .build());
    }

    @Override
    public void deliverySuccess(UUID deliveryId) {
        setDeliveryState(deliveryId, DeliveryState.DELIVERED);
        orderClient.orderDelivered(getDeliveryById(deliveryId).getOrderId());
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        setDeliveryState(deliveryId, DeliveryState.FAILED);
        orderClient.orderDeliveryFailed(getDeliveryById(deliveryId).getOrderId());
    }

    @Override
    public BigDecimal calcCostDelivery(OrderDto order) {
        BigDecimal deliveryCost = BigDecimal.valueOf(DELIVERY_BASE_COST);

        Delivery delivery = getDeliveryById(order.getDeliveryId());

        if ("ADDRESS_2".equals(delivery.getFromAddress().getStreet())) {
            deliveryCost = deliveryCost.multiply(BigDecimal.TWO).add(deliveryCost);
        }

        if (order.getFragile()) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(0.2)).add(deliveryCost);
        }

        deliveryCost = deliveryCost.add(BigDecimal.valueOf(order.getDeliveryWeight()).multiply(BigDecimal.valueOf(0.3)));
        deliveryCost = deliveryCost.add(BigDecimal.valueOf(order.getDeliveryVolume()).multiply(BigDecimal.valueOf(0.2)));

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(0.2)).add(deliveryCost);
        }

        return deliveryCost;
    }

    private void setDeliveryState(UUID deliveryId, DeliveryState state) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setState(state);
        deliveryRepository.save(delivery);
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(() -> new NoDeliveryFoundException("Доставка id=" + deliveryId + " не найдена!"));
    }
}
