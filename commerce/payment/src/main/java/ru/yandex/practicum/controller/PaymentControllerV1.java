package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentControllerV1 {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody OrderDto order) {
        return paymentService.createPayment(order);
    }

    @PostMapping("/refund")
    public void paymentSuccess(@RequestBody UUID paymentId) {
        paymentService.setPaymentSuccess(paymentId);
    }

    @PostMapping("/failed")
    public void paymentRejected(@RequestBody UUID paymentId) {
        paymentService.setPaymentRejected(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal getProductCost(@RequestBody OrderDto order) {
        return paymentService.getProductCost(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal getTotalCost(@RequestBody OrderDto order) {
        return paymentService.getTotalCost(order);
    }
}
