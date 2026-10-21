package com.example.server.dtos;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDeleteResponseDTO {
    private int id;
    private String message;
}
