package com.example.server.utils;

import com.example.server.enums.TicketPriority;
import jakarta.persistence.AttributeConverter;

import javax.print.attribute.Attribute;

public class PriorityConverter implements AttributeConverter<TicketPriority, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TicketPriority attribute) {
        if (attribute == null) return null;
        if (attribute == TicketPriority.LOW) return 1;
        if (attribute == TicketPriority.MEDIUM) return 2;
        if (attribute == TicketPriority.HIGH) return 3;
        return 4;
    }

    @Override
    public TicketPriority convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        if (dbData == 1) return TicketPriority.LOW;
        if (dbData == 2) return TicketPriority.MEDIUM;
        if (dbData == 3) return TicketPriority.HIGH;
        return TicketPriority.CRITICAL;
    }
}
