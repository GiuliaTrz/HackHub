package com.project.hackhub.repository;

import com.project.hackhub.model.hackathon.TimeInterval;
import com.project.hackhub.model.hackathon.Location;
import com.project.hackhub.model.hackathon.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    boolean existsByLocationAndTimeInterval(Location location, TimeInterval timeInterval);
}
