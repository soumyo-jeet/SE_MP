package com.example.server.controllers;

import com.example.server.dtos.TicketRequestDTO;
import com.example.server.dtos.TicketResponseDTO;
import com.example.server.dtos.TicketStatusUpdateDTO;
import com.example.server.enums.TicketStatus;
import com.example.server.models.Ticket;
import com.example.server.models.UserDetailsImpl;
import com.example.server.services.StaffService;
import com.example.server.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    StaffService staffService;
    TicketService ticketService;

    public StaffController(StaffService staffService, TicketService ticketService) {
        this.staffService = staffService;
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getAssignedTickets() {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long staffId = userDetails.getId();

        List<Ticket> assignments = ticketService.findByStaffId(staffId);
        List<TicketResponseDTO> responseDTOs = new ArrayList<>();

        for (Ticket ticket : assignments) {
            responseDTOs.add(TicketResponseDTO.getTicketResponseDTO(ticket));
        }
        return ResponseEntity.ok(responseDTOs);
    }


    @PutMapping("/ticket/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(
            @PathVariable("id") Long ticketId,
            @RequestBody TicketStatusUpdateDTO ticketStatusUpdateDTO) {

        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long staffId = userDetails.getId();

        Ticket updatedTicket = ticketService.
                updateStatus(ticketId, staffId, ticketStatusUpdateDTO);
        return new ResponseEntity<>(TicketResponseDTO.getTicketResponseDTO(updatedTicket), HttpStatus.OK);
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(
            @PathVariable("id") Long ticketId) {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long staffId = userDetails.getId();

        Ticket ticket = ticketService.findById(ticketId, staffId, true);
        return new ResponseEntity<>(TicketResponseDTO.getTicketResponseDTO(ticket), HttpStatus.OK);
    }
}
