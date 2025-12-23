package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    void setPaymentSuccess(UUID paymentId);

    void setPaymentRejected(UUID paymentId);

    BigDecimal getProductCost(OrderDto order);

    BigDecimal getTotalCost(OrderDto order);
}
