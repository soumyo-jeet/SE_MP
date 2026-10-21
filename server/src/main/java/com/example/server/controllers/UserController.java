package com.example.server.controllers;

import com.example.server.dtos.TicketDeleteResponseDTO;
import com.example.server.dtos.TicketRequestDTO;
import com.example.server.dtos.TicketResponseDTO;
import com.example.server.dtos.UserRequestDTO;
import com.example.server.models.Ticket;
import com.example.server.models.User;
import com.example.server.models.UserDetailsImpl;
import com.example.server.services.TicketService;
import com.example.server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.ResponseAPDU;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    private TicketService ticketService;

    public UserController(UserService userService,  TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    // User actions
    @PostMapping("/ticket")
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestBody TicketRequestDTO ticketRequestDTO
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = userDetails.getId();

        Ticket ticket = ticketService.createTicket(ticketRequestDTO, userId);

        TicketResponseDTO ticketResponseDTO = TicketResponseDTO
                .getTicketResponseDTO(ticket);

        return new ResponseEntity<>(ticketResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = userDetails.getId();

        List<Ticket> tickets = userService.findAllTickets(userId);

        List<TicketResponseDTO> ticketResponseDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketResponseDTOs.add(TicketResponseDTO.getTicketResponseDTO(ticket));
        }
        return new ResponseEntity<>(ticketResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/ticket/{ticket-id}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long ticketId) {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = userDetails.getId();

        Ticket ticket = ticketService.findById(ticketId, userId);

        return ResponseEntity.ok(TicketResponseDTO.getTicketResponseDTO(ticket));
    }

    @DeleteMapping("/ticket/{ticket-id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId) {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = userDetails.getId();

        ticketService.delete(ticketId, userId);

        return ResponseEntity.ok("Deleted successfully.");
    }
}
