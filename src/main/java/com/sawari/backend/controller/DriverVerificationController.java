package com.sawari.backend.controller;

import com.sawari.backend.entity.DriverVerification;
import com.sawari.backend.repository.DriverVerificationRepository;
import com.sawari.backend.repository.UserRepository;
import com.sawari.backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "http://localhost:3000")
public class DriverVerificationController {

    @Autowired
    private DriverVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> submit(
            @RequestParam("driverId") Long driverId,
            @RequestParam("fullName") String fullName,
            @RequestParam("licenseNo") String licenseNo,
            @RequestParam("vehicleNo") String vehicleNo,
            @RequestParam("aadharNo") String aadharNo,
            @RequestParam("aadharPhoto") MultipartFile aadharPhoto,
            @RequestParam("licensePhoto") MultipartFile licensePhoto,
            @RequestParam("vehicleRcPhoto") MultipartFile vehicleRcPhoto,
            @RequestParam("selfie") MultipartFile selfie
    ) {
        try {
            verificationRepository.findByDriverId(driverId)
                    .ifPresent(verificationRepository::delete);

            DriverVerification v = new DriverVerification();
            v.setDriverId(driverId);
            v.setFullName(fullName);
            v.setLicenseNo(licenseNo);
            v.setVehicleNo(vehicleNo);
            v.setAadharNo(aadharNo);
            v.setStatus("PENDING");

            v.setAadharPhotoPath(fileStorageService.saveFile(aadharPhoto, "driver/aadhar"));
            v.setLicensePhotoPath(fileStorageService.saveFile(licensePhoto, "driver/license"));
            v.setVehicleRcPhotoPath(fileStorageService.saveFile(vehicleRcPhoto, "driver/vehicle"));
            v.setSelfiePath(fileStorageService.saveFile(selfie, "driver/selfie"));

            return ResponseEntity.ok(verificationRepository.save(v));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<DriverVerification> getDriverVerification(@PathVariable Long driverId) {
        return verificationRepository.findByDriverId(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    public List<DriverVerification> getAll() {
        return verificationRepository.findAll();
    }

    // ✅ Admin approves — also sets verified = true on User
    @PutMapping("/{id}/approve")
    public ResponseEntity<DriverVerification> approve(@PathVariable Long id) {
        return verificationRepository.findById(id)
                .map(v -> {
                    v.setStatus("APPROVED");
                    verificationRepository.save(v);

                    userRepository.findById(v.getDriverId())
                            .ifPresent(user -> {
                                user.setVerified(true);
                                userRepository.save(user);
                            });

                    return ResponseEntity.ok(v);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Admin rejects — also sets verified = false on User
    @PutMapping("/{id}/reject")
    public ResponseEntity<DriverVerification> reject(@PathVariable Long id) {
        return verificationRepository.findById(id)
                .map(v -> {
                    v.setStatus("REJECTED");

                    userRepository.findById(v.getDriverId())
                            .ifPresent(user -> {
                                user.setVerified(false);
                                userRepository.save(user);
                            });

                    return ResponseEntity.ok(verificationRepository.save(v));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}