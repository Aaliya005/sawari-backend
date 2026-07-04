package com.sawari.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "driver_verifications")
public class DriverVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;
    private String fullName;
    private String licenseNo;
    private String vehicleNo;
    private String aadharNo;
    private String status;

    // file paths stored on server
    private String aadharPhotoPath;
    private String licensePhotoPath;
    private String vehicleRcPhotoPath;
    private String selfiePath;
}