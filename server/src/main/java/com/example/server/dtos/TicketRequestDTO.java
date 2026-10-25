package com.example.server.dtos;

import com.example.server.enums.TicketCatagory;
import com.example.server.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TicketRequestDTO {

    @NotBlank(message = "Please provide a title for the ticket.")
    private String title;

    @NotBlank(message = "Please describe the issue you are experiencing.")
    private String description;

    @NotNull(message = "Please select a category for the ticket.")
    private TicketCatagory cat;

    @NotNull(message = "Please select a priority level for the ticket.")
    private TicketPriority priority;
}
