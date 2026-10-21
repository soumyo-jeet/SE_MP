package com.example.server.controllers;

import com.example.server.dtos.StaffRequestDTO;
import com.example.server.dtos.StaffResponseDTO;
import com.example.server.dtos.TicketResponseDTO;
import com.example.server.enums.StaffDeptartment;
import com.example.server.models.Staff;
import com.example.server.models.Ticket;
import com.example.server.models.UserDetailsImpl;
import com.example.server.repositories.TicketRepository;
import com.example.server.services.StaffService;
import com.example.server.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    StaffService staffService;
    TicketService ticketService;


    public AdminController (StaffService staffService, TicketService ticketService) {
        this.staffService = staffService;
        this.ticketService = ticketService;
    }

    // staff management endpoints
    @PostMapping("/staff")
    public ResponseEntity<StaffResponseDTO> registerStaff(
            @RequestBody StaffRequestDTO staffRequestDTO) {
        Staff staff = staffService.createStaff(staffRequestDTO);
        StaffResponseDTO staffResponseDTO = StaffResponseDTO.getStaffResponseDTO(staff);
        return new ResponseEntity<>(staffResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/staffs")
    public ResponseEntity<List<StaffResponseDTO>> getStaffs() {
        List<Staff> staffs = staffService.findAll();
        List<StaffResponseDTO> staffResponseDTOs = new ArrayList<>();
        for (Staff staff : staffs) {
            staffResponseDTOs.add(StaffResponseDTO.getStaffResponseDTO(staff));
        }
        return new ResponseEntity<>(staffResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/staffs/{department}")
    public ResponseEntity<List<StaffResponseDTO>> getStaff(
            @PathVariable("department")
            StaffDeptartment department) {
        List<Staff> staffs = staffService.findActiveStaffsByDepartment(department);
        List<StaffResponseDTO> staffResponseDTOs = new ArrayList<>();
        for (Staff staff : staffs) {
            staffResponseDTOs.add(StaffResponseDTO.getStaffResponseDTO(staff));
        }

        return new ResponseEntity<>(staffResponseDTOs, HttpStatus.OK);
    }


    // ticket management endpoints
    @PutMapping("/ticket/assign")
    public ResponseEntity<TicketResponseDTO> assignTicket(
            @RequestParam("ticket-id") Long ticketId,
            @RequestParam("staff-id") Long staffId) {
        Ticket ticket = ticketService.assignStaff(staffId, ticketId);
        TicketResponseDTO dto =
                TicketResponseDTO.getTicketResponseDTO(ticket);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/ticket/reassign")
    public ResponseEntity<TicketResponseDTO> reassignTicket(
            @RequestParam("ticket-id") Long ticketId,
            @RequestParam("staff-id") Long staffId) {
        Ticket ticket = ticketService.updateAssignedStaff(staffId, ticketId);
        TicketResponseDTO dto =
                TicketResponseDTO.getTicketResponseDTO(ticket);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        List<TicketResponseDTO> ticketResponseDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketResponseDTOs.add(TicketResponseDTO.getTicketResponseDTO(ticket));
        }
        return new ResponseEntity<>(ticketResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(
            @PathVariable("id") Long ticketId) {
        Ticket ticket = ticketService.findById(ticketId);
        return new ResponseEntity<>(TicketResponseDTO.getTicketResponseDTO(ticket), HttpStatus.OK);
    }

    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Long id) {
        ticketService.delete(id);
        return new ResponseEntity<>("Ticket has been deleted", HttpStatus.OK);
    }
}
