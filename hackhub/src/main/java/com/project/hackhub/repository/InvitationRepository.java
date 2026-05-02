package com.project.hackhub.repository;

import com.project.hackhub.model.team.Invitation;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    boolean existsBySenderAndAddresseeAndPendingTrue(Team sender, User addressee);
}
