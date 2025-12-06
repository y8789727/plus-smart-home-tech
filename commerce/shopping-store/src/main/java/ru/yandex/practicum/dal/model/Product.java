package ru.yandex.practicum.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductState;
import ru.yandex.practicum.dto.product.QuantityState;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    private String description;

    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "product_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "image_src")
    private String imageSrc;

    private BigDecimal price;
}
