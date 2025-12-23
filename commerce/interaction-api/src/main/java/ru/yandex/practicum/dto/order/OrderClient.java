package ru.yandex.practicum.dto.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient {

    @PostMapping("/api/v1/order/payment")
    OrderDto orderPaid(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/failed")
    OrderDto orderPayFailed(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/delivery")
    OrderDto orderDelivered(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto orderDeliveryFailed(@RequestBody UUID orderId);
}
