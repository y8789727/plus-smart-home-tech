package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(String userId);
}
