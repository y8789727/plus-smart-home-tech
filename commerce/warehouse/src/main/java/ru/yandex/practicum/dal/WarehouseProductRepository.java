package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.WarehouseProduct;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Integer> {

    Optional<WarehouseProduct> findByWarehouseIdAndProductId(String warehouseId, UUID productId);

    List<WarehouseProduct> findByWarehouseIdAndProductIdIn(String warehouseId, Set<UUID> productIds);
}
