package com.example.server.dtos;

import com.example.server.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatusUpdateDTO {
    private TicketStatus ticketStatus;
    private String emailSubject;
    private String emailBody;
}
