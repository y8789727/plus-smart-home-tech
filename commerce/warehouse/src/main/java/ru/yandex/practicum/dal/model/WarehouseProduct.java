package ru.yandex.practicum.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "warehouse_product")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "productId", nullable = false)
    private String productId;

    @Column(name = "fragile", nullable = false)
    private Boolean fragile;

    private Double width;
    private Double height;
    private Double depth;
    private Double weight;
    private Double quantity;

}
