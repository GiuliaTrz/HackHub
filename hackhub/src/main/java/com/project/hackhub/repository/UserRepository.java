package com.project.hackhub.repository;

import com.project.hackhub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByPersonalData_Email(String personalDataEmail);

    Optional<User> findByPersonalData_UserName(String personalDataUserName);
}