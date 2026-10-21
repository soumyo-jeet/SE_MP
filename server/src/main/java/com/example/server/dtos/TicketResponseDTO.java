package com.example.server.dtos;

import com.example.server.enums.TicketCatagory;
import com.example.server.enums.TicketPriority;
import com.example.server.enums.TicketStatus;
import com.example.server.models.Ticket;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketResponseDTO {
    private Long id;
    private TicketStatus status;
    private TicketCatagory catagory;
    private TicketPriority priority;
    private String title;
    private String description;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long staffId;
    private String staffName;
    private String staffEmail;

    private LocalDateTime createdAt;

    public static TicketResponseDTO getTicketResponseDTO(Ticket ticket) {

        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();

        ticketResponseDTO.setId(ticket.getId());
        ticketResponseDTO.setTitle(ticket.getTitle());
        ticketResponseDTO.setDescription(ticket.getDescription());
        ticketResponseDTO.setStatus(ticket.getStatus());
        ticketResponseDTO.setPriority(ticket.getPriority());
        ticketResponseDTO.setCatagory(ticket.getCat());

        ticketResponseDTO.setUserId(ticket.getUser().getId());
        ticketResponseDTO.setUserName(ticket.getUser().getUsername());
        ticketResponseDTO.setUserEmail(ticket.getUser().getEmail());

        if (ticket.getStaff() != null) {
            ticketResponseDTO.setStaffId(ticket.getStaff().getId());
            ticketResponseDTO.setStaffName(ticket.getStaff().getUsername());
            ticketResponseDTO.setStaffEmail(ticket.getStaff().getEmail());
        }

        ticketResponseDTO.setCreatedAt(ticket.getCreated());

        return ticketResponseDTO;
    }
}
