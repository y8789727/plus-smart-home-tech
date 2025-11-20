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

@Entity
@Table(name = "sensors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    private String id;

    @Column(name = "hub_id")
    private String hubId;

    @Enumerated(EnumType.STRING)
    private SensorType type;
}
