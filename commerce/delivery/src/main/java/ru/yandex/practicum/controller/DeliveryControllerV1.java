package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryControllerV1 {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @PostMapping("/picked")
    public void pickDelivery(@RequestBody UUID deliveryId) {
        deliveryService.pickDelivery(deliveryId);
    }

    @PostMapping("/successful")
    public void deliverySuccess(@RequestBody UUID deliveryId) {
        deliveryService.deliverySuccess(deliveryId);
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody UUID deliveryId) {
        deliveryService.deliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    public BigDecimal calcCostDelivery(@RequestBody OrderDto order) {
        return deliveryService.calcCostDelivery(order);
    }

}
