package com.example.server.dtos;

import com.example.server.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatusUpdateDTO {

    @NotNull(message = "Please select a valid ticket status.")
    private TicketStatus ticketStatus;

    @NotBlank(message = "Please provide an email subject.")
    private String emailSubject;

    @NotBlank(message = "Please provide a message to include in the email.")
    private String emailBody;
}
