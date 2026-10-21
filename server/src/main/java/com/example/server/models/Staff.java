package com.example.server.models;

import com.example.server.enums.StaffDeptartment;
import com.example.server.enums.StaffStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private StaffDeptartment department;
    @Enumerated(EnumType.STRING)
    private StaffStatus status;

    @OneToMany(mappedBy = "staff")
    private List<Ticket> assignedTickets;
}
