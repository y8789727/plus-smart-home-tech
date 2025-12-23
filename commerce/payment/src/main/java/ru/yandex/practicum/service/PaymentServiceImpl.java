package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.PaymentRepository;
import ru.yandex.practicum.dal.model.Payment;
import ru.yandex.practicum.dal.model.PaymentState;
import ru.yandex.practicum.dto.PaymentMapper;
import ru.yandex.practicum.dto.order.OrderClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.product.ProductDto;
import ru.yandex.practicum.dto.store.ShoppingStoreClient;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Double VAT_RATE = 10.0;

    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        return PaymentMapper.mapToDto(paymentRepository.save(Payment.builder()
                        .paymentId(UUID.randomUUID())
                        .totalPayment(order.getTotalPrice())
                        .productPrice(order.getProductPrice())
                        .deliveryTotal(order.getDeliveryPrice())
                        .orderId(order.getOrderId())
                        .state(PaymentState.PENDING)
                        .build()));
    }

    @Override
    public void setPaymentSuccess(UUID paymentId) {
        setPaymentState(paymentId, PaymentState.SUCCESS);
        orderClient.orderPaid(getPaymentById(paymentId).getOrderId());
    }

    @Override
    public void setPaymentRejected(UUID paymentId) {
        setPaymentState(paymentId, PaymentState.FAILED);
        orderClient.orderPayFailed(getPaymentById(paymentId).getOrderId());
    }

    @Override
    public BigDecimal getProductCost(OrderDto order) {
        return order.getProducts().entrySet().stream()
                .map(entry -> {
                    ProductDto productInfo = shoppingStoreClient.getProductById(entry.getKey());
                    return productInfo.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto order) {
        if (order.getProductPrice() == null || order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Не хватает данных для расчета суммы по заказу");
        }

        return order.getProductPrice()
                .multiply(BigDecimal.valueOf((1.0 + (VAT_RATE/100.0))))
                .add(order.getDeliveryPrice());
    }

    private void setPaymentState(UUID paymentId, PaymentState state) {
        Payment payment = getPaymentById(paymentId);
        payment.setState(state);
        paymentRepository.save(payment);
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new NoPaymentFoundException("Не найден платеж id=" + paymentId));
    }
}
