package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.OrderProduct;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderProductsRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> getProductsByOrderId(UUID orderId);

    List<OrderProduct> findByOrderIdIn(Set<UUID> orderIds);
}
