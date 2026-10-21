package com.example.server.dtos;

import com.example.server.enums.TicketCatagory;
import com.example.server.enums.TicketPriority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TicketRequestDTO {
    private String title;
    private String description;
    private TicketCatagory cat;
    private TicketPriority priority;
}
