package com.sawari.backend.repository;

import com.sawari.backend.entity.SosAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SosAlertRepository extends JpaRepository<SosAlert, Long> {
    List<SosAlert> findByUserId(Long userId);
    List<SosAlert> findByStatus(String status);
}