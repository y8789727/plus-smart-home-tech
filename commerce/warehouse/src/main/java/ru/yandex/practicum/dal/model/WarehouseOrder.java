package ru.yandex.practicum.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "warehouse_order")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseOrder {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;
}
