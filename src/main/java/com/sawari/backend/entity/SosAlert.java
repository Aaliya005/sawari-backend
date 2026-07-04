package com.sawari.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sos_alerts")
public class SosAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String role;
    private String message;
    private String status; // SENT, RESOLVED

    private String rideFrom;
    private String rideTo;
    private String rideDate;
    private String rideTime;

    private String triggeredAt;
}