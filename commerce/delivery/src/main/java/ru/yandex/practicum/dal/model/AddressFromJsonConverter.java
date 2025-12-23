package ru.yandex.practicum.dal.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.yandex.practicum.dto.warehouse.AddressDto;

@Converter
public class AddressFromJsonConverter implements AttributeConverter<AddressDto, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AddressDto addressDto) {
        try {
            return objectMapper.writeValueAsString(addressDto);
        } catch (JsonProcessingException jpe) {
            return "";
        }
    }

    @Override
    public AddressDto convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(s, AddressDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
