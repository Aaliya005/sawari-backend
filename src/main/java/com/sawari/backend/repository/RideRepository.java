package com.sawari.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sawari.backend.entity.Ride;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByDriverId(Long driverId);
    List<Ride> findByStatus(String status);
}