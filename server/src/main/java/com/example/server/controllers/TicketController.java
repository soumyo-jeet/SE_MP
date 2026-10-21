package com.example.server.controllers;

import com.example.server.dtos.TicketRequestDTO;
import com.example.server.dtos.TicketResponseDTO;
import com.example.server.dtos.TicketStatusUpdateDTO;
import com.example.server.enums.TicketStatus;
import com.example.server.models.Ticket;
import com.example.server.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }




    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(
            @PathVariable Long id
    ) {
        Ticket ticket = ticketService.findById(id);

        TicketResponseDTO ticketResponseDTO = TicketResponseDTO
                .getTicketResponseDTO(ticket);

        return new ResponseEntity<>(ticketResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        List<TicketResponseDTO> ticketResponseDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketResponseDTOs.add(TicketResponseDTO.getTicketResponseDTO(ticket));
        }
        return new ResponseEntity<>(ticketResponseDTOs, HttpStatus.OK);
    }


}
