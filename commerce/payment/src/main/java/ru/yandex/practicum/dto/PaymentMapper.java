package ru.yandex.practicum.dto;

import ru.yandex.practicum.dal.model.Payment;
import ru.yandex.practicum.dto.payment.PaymentDto;

public class PaymentMapper {
    public static PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .productTotal(payment.getProductPrice())
                .deliveryTotal(payment.getDeliveryTotal())
                .build();
    }
}
