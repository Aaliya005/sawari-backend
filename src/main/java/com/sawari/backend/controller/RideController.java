package com.sawari.backend.controller;

import com.sawari.backend.entity.Ride;
import com.sawari.backend.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "http://localhost:3000")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    // Driver creates a ride
    @PostMapping
    public Ride createRide(@RequestBody Ride ride) {
        ride.setStatus("ACTIVE");
        return rideRepository.save(ride);
    }

    // Passenger searches all active rides
    @GetMapping
    public List<Ride> getAllRides() {
        return rideRepository.findByStatus("ACTIVE");
    }

    // Driver sees their own rides
    @GetMapping("/driver/{driverId}")
    public List<Ride> getDriverRides(@PathVariable Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    // Get single ride by id
    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        return rideRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin sees all rides
    @GetMapping("/admin/all")
    public List<Ride> getAllRidesAdmin() {
        return rideRepository.findAll();
    }

    // Driver cancels a ride
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long id) {
        return rideRepository.findById(id)
                .map(ride -> {
                    ride.setStatus("CANCELLED");
                    return ResponseEntity.ok(rideRepository.save(ride));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Driver completes a ride
    @PutMapping("/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long id) {
        return rideRepository.findById(id)
                .map(ride -> {
                    ride.setStatus("COMPLETED");
                    return ResponseEntity.ok(rideRepository.save(ride));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}