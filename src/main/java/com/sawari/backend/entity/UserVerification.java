package com.sawari.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_verifications")
public class UserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String fullName;
    private String aadharNo;
    private String collegeOrCompanyId;
    private String status;

    // file paths stored on server
    private String aadharPhotoPath;
    private String collegeIdPhotoPath;
    private String selfiePath;
}