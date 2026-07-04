package com.sawari.backend.repository;

import com.sawari.backend.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    Optional<UserVerification> findByUserId(Long userId);
    List<UserVerification> findByStatus(String status);
}