package ru.yandex.practicum.dto.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;

@FeignClient(name = "payment")
public interface PaymentClient {
    @PostMapping("/api/v1/payment/productCost")
    BigDecimal productCost(@RequestBody OrderDto order);

    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal getTotalCost(@RequestBody OrderDto order);

    @PostMapping("/api/v1/payment")
    PaymentDto payment(@RequestBody OrderDto order);
}
