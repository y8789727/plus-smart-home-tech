package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.WarehouseOrder;

import java.util.UUID;

public interface WarehouseOrderRepository extends JpaRepository<WarehouseOrder, UUID> {
}
