package ru.yandex.practicum.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.ToString;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.warehouse.AddressDto;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;

    @Column(name = "total_weight")
    private BigDecimal totalWeight;

    @Column(name = "total_volume", nullable = false)
    private BigDecimal totalVolume;

    @Column(name = "fragile")
    private Boolean fragile;

    @Convert(converter = AddressFromJsonConverter.class)
    @Column(name = "from_address")
    private AddressDto fromAddress;

    @Convert(converter = AddressFromJsonConverter.class)
    @Column(name = "to_address")
    private AddressDto toAddress;

}
