package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.OrderBooking;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, Integer> {
}
