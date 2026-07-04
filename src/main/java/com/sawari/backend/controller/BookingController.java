package com.sawari.backend.controller;

import com.sawari.backend.entity.Booking;
import com.sawari.backend.entity.Ride;
import com.sawari.backend.repository.BookingRepository;
import com.sawari.backend.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RideRepository rideRepository;

    // Passenger books a ride
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        // ✅ Check if passenger already has an active booking
        List<Booking> existing = bookingRepository.findByPassengerId(booking.getPassengerId());
        boolean hasActive = existing.stream()
                .anyMatch(b -> b.getStatus().equals("CONFIRMED") || b.getStatus().equals("PENDING"));

        if (hasActive) {
            return ResponseEntity.badRequest().body("You already have an active booking. Cancel it before booking another ride.");
        }

        // ✅ Check ride exists and has seats
        Ride ride = rideRepository.findById(booking.getRideId()).orElse(null);

        if (ride == null) {
            return ResponseEntity.badRequest().body("Ride not found");
        }
        if (ride.getSeats() <= 0) {
            return ResponseEntity.badRequest().body("No seats available");
        }
        if (ride.getStatus().equals("CANCELLED")) {
            return ResponseEntity.badRequest().body("Ride is cancelled");
        }

        // ✅ Decrement seats
        ride.setSeats(ride.getSeats() - 1);
        rideRepository.save(ride);

        booking.setStatus("CONFIRMED");
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    // Passenger sees their bookings
    @GetMapping("/passenger/{passengerId}")
    public List<Booking> getPassengerBookings(@PathVariable Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    // Driver sees bookings for their ride
    @GetMapping("/ride/{rideId}")
    public List<Booking> getRideBookings(@PathVariable Long rideId) {
        return bookingRepository.findByRideId(rideId);
    }

    // Passenger cancels booking — also restores seat
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus("CANCELLED");
                    bookingRepository.save(booking);

                    // ✅ Restore seat when booking cancelled
                    rideRepository.findById(booking.getRideId())
                            .ifPresent(ride -> {
                                ride.setSeats(ride.getSeats() + 1);
                                rideRepository.save(ride);
                            });

                    return ResponseEntity.ok(booking);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}