package com.sawari.backend.controller;

import com.sawari.backend.entity.UserVerification;
import com.sawari.backend.repository.UserVerificationRepository;
import com.sawari.backend.repository.UserRepository;
import com.sawari.backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user-verification")
@CrossOrigin(origins = "http://localhost:3000")
public class UserVerificationController {

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> submit(
            @RequestParam("userId") Long userId,
            @RequestParam("fullName") String fullName,
            @RequestParam("aadharNo") String aadharNo,
            @RequestParam("collegeOrCompanyId") String collegeOrCompanyId,
            @RequestParam("aadharPhoto") MultipartFile aadharPhoto,
            @RequestParam("collegeIdPhoto") MultipartFile collegeIdPhoto,
            @RequestParam("selfie") MultipartFile selfie
    ) {
        try {
            userVerificationRepository.findByUserId(userId)
                    .ifPresent(userVerificationRepository::delete);

            UserVerification v = new UserVerification();
            v.setUserId(userId);
            v.setFullName(fullName);
            v.setAadharNo(aadharNo);
            v.setCollegeOrCompanyId(collegeOrCompanyId);
            v.setStatus("PENDING");

            v.setAadharPhotoPath(fileStorageService.saveFile(aadharPhoto, "user/aadhar"));
            v.setCollegeIdPhotoPath(fileStorageService.saveFile(collegeIdPhoto, "user/college"));
            v.setSelfiePath(fileStorageService.saveFile(selfie, "user/selfie"));

            return ResponseEntity.ok(userVerificationRepository.save(v));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserVerification> getUserVerification(@PathVariable Long userId) {
        return userVerificationRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    public List<UserVerification> getAll() {
        return userVerificationRepository.findAll();
    }

    // ✅ Admin approves — also sets verified = true on User
    @PutMapping("/{id}/approve")
    public ResponseEntity<UserVerification> approve(@PathVariable Long id) {
        return userVerificationRepository.findById(id)
                .map(v -> {
                    v.setStatus("APPROVED");
                    userVerificationRepository.save(v);

                    userRepository.findById(v.getUserId())
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
    public ResponseEntity<UserVerification> reject(@PathVariable Long id) {
        return userVerificationRepository.findById(id)
                .map(v -> {
                    v.setStatus("REJECTED");

                    userRepository.findById(v.getUserId())
                            .ifPresent(user -> {
                                user.setVerified(false);
                                userRepository.save(user);
                            });

                    return ResponseEntity.ok(userVerificationRepository.save(v));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}