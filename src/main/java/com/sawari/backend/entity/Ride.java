package com.sawari.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;      // links to users.id
    private String fromLocation;
    private String toLocation;
    private String date;
    private String time;
    private int seats;
    private boolean womenOnly;
    private String community;
    private String status;      // ACTIVE, CANCELLED, COMPLETED
}