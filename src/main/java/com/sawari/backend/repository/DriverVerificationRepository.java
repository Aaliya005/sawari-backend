package com.sawari.backend.repository;

import com.sawari.backend.entity.DriverVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DriverVerificationRepository extends JpaRepository<DriverVerification, Long> {
    Optional<DriverVerification> findByDriverId(Long driverId);
    List<DriverVerification> findByStatus(String status);
}