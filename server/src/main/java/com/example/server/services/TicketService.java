package com.example.server.services;

import com.example.server.dtos.TicketRequestDTO;
import com.example.server.dtos.TicketStatusUpdateDTO;
import com.example.server.enums.TicketStatus;
import com.example.server.models.Staff;
import com.example.server.models.Ticket;
import com.example.server.models.User;
import com.example.server.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private TicketRepository ticketRepository;
    private UserService userService;
    private StaffService staffService;
    private EmailService emailService;

    public TicketService(TicketRepository ticketRepository, UserService userService, StaffService staffService, EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.staffService = staffService;
        this.emailService = emailService;
    }

    @Transactional
    public Ticket createTicket (TicketRequestDTO ticketRequestDTO, Long userId) {
        User receivedUser = userService.findById(userId);

        Ticket ticket = new Ticket();

        ticket.setTitle(ticketRequestDTO.getTitle());
        ticket.setDescription(ticketRequestDTO.getDescription());
        ticket.setCat(ticketRequestDTO.getCat());
        ticket.setPriority(ticketRequestDTO.getPriority());
        ticket.setUser(receivedUser);
        ticket.setStatus(TicketStatus.OPEN);
        receivedUser.getTickets().add(ticket);

        Ticket newTicket = ticketRepository.save(ticket);
        return newTicket;
    }

    @Transactional
    public Ticket findById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(!ticket.isPresent()) throw new RuntimeException("Ticket not found");
        return ticket.get();
    }

    @Transactional
    public Ticket findById(Long id, Long userId) {
        Ticket ticket = findById(id);
        if(!ticket.getUser().getId().equals(userId))
            throw new RuntimeException("Not eligible for this ticket");
        return ticket;
    }

    @Transactional
    public Ticket findById(Long id, Long staffId, Boolean flag) {
        Ticket ticket = findById(id);
        if(
                ticket.getStatus().equals(TicketStatus.OPEN) ||
                        !ticket.getStaff().getId().equals(staffId))
            throw new RuntimeException("Not eligible for this ticket");
        return ticket;
    }

    @Transactional
    public List<Ticket> findAll() {
        Sort sort = Sort
                .by("priority")
                .descending()
                .and(
                        Sort
                                .by("created"));
        List<Ticket> tickets = ticketRepository.findAll(sort);

        return tickets;
    }


    @Transactional
    public List<Ticket> findByStaffId(Long staffId) {
        Sort sort = Sort
                .by("priority")
                .descending()
                .and(
                        Sort
                                .by("created"));
        List<Ticket> tickets = ticketRepository.findByStaffId(sort, staffId);

        return tickets;
    }



    @Transactional
    public Ticket assignStaff(Long staffId, Long ticketId) {
        Ticket ticket = findById(ticketId);
        Staff staff = staffService.findById(staffId);

        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new RuntimeException("Ticket is already assigned.");
        }

        if (staff.getAssignedTickets().size() >= 3) {
            throw new RuntimeException("Staff is busy.");
        }

        ticket.setStaff(staff);
        ticket.setStatus(TicketStatus.ASSIGNED);

        staff.getAssignedTickets().add(ticket);

        return ticket;
    }

    @Transactional
    public Ticket updateAssignedStaff(Long staffId, Long ticketId) {
        Ticket ticket = findById(ticketId);
        Staff staff = staffService.findById(staffId);

        if(ticket.getStatus().equals(TicketStatus.OPEN))
            throw new RuntimeException("Ticket is yet not assigned at all.");

        ticket.getStaff().getAssignedTickets().remove(ticket);
        ticket.setStaff(staff);
        staff.getAssignedTickets().add(ticket);
        return ticket;
    }

    @Transactional
    public Ticket updateStatus(
            Long ticketId, Long staffId, TicketStatusUpdateDTO ticketStatusUpdateDTO) {
        Ticket ticket = findById(ticketId);
        Staff staff = staffService.findById(staffId);
        User owner = ticket.getUser();

        if(ticket.getStatus().equals(TicketStatus.OPEN))
            throw new RuntimeException("Ticket is yet not assigned at all.");
        if(!ticket.getStaff().getId().equals(staff.getId()))
            throw new RuntimeException("This staff is not assigned for this ticket");
        if(ticket.getStatus() == ticketStatusUpdateDTO.getTicketStatus())
            throw new RuntimeException("Already updated.");

        ticket.setStatus(ticketStatusUpdateDTO.getTicketStatus());

        try{
            emailService.sendMail(
                    "samajdarsoumyajeet0@gmail.com",
                    ticketStatusUpdateDTO.getEmailSubject(),
                    owner.getUsername(),
                    ticketId,
                    ticketStatusUpdateDTO.getTicketStatus(),
                    ticketStatusUpdateDTO.getEmailBody()
            );
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Email sending failed.");
        }

        return ticket;
    }

    public void delete(Long ticketId, Long  userId) {
        Ticket ticket = findById(ticketId);
        if(!ticket.getUser().getId().equals(userId))
            throw new RuntimeException("Not eligible for this ticket");

        ticketRepository.delete(ticket);
    }

    public void delete(Long ticketId) {
        Ticket ticket = findById(ticketId);
        ticketRepository.delete(ticket);
    }

}
