package com.sawari.backend.controller;

import com.sawari.backend.entity.SosAlert;
import com.sawari.backend.repository.SosAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sos")
@CrossOrigin(origins = "http://localhost:3000")
public class SosAlertController {

    @Autowired
    private SosAlertRepository sosAlertRepository;

    // User triggers SOS
    @PostMapping
    public ResponseEntity<SosAlert> triggerSOS(@RequestBody SosAlert alert) {
        alert.setStatus("SENT");
        alert.setTriggeredAt(LocalDateTime.now().toString());
        return ResponseEntity.ok(sosAlertRepository.save(alert));
    }

    // Admin sees all SOS alerts
    @GetMapping("/admin/all")
    public List<SosAlert> getAll() {
        return sosAlertRepository.findAll();
    }

    // Admin resolves an SOS
    @PutMapping("/{id}/resolve")
    public ResponseEntity<SosAlert> resolve(@PathVariable Long id) {
        return sosAlertRepository.findById(id)
                .map(alert -> {
                    alert.setStatus("RESOLVED");
                    return ResponseEntity.ok(sosAlertRepository.save(alert));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // User sees their own SOS history
    @GetMapping("/user/{userId}")
    public List<SosAlert> getUserAlerts(@PathVariable Long userId) {
        return sosAlertRepository.findByUserId(userId);
    }
}