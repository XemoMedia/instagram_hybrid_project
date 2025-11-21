package com.example.instagram.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return mapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JsonNode to JSON String", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            return mapper.readTree(dbData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON String to JsonNode", e);
        }
    }
}

