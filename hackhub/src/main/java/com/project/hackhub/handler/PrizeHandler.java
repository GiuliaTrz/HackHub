package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Money;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.user.User;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrizeHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository utenteRepository;

    /**
     * Claims the monetary prize for a team member winner.
     * The prize is divided equally among team members.
     * Simulates redirection to an external payment system.
     *
     * @param userId      ID of the team member requesting the prize redemption
     * @param hackathonId ID of the hackathon
     * @throws IllegalStateException if the hackathon has no winner or the prize has already been claimed
     * @author Giulia Trozzi
     */
    @Transactional
    public void claimPrize(UUID userId, UUID hackathonId) {
        User user = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        // Check that the hackathon has ended, and therefore is in the "CONCLUDED" state
        if (hackathon.getState().getStateType() != HackathonStateType.CONCLUDED) {
            throw new IllegalStateException("Hackathon has not concluded yet");
        }

        Team winner = hackathon.getWinner();
        if (winner == null) {
            throw new IllegalStateException("No winning team proclaimed for this hackathon");
        }

        if (!winner.getTeamMembersList().contains(user)) {
            throw new UnsupportedOperationException("Only members of the winning team can collect the prize");
        }

        Money totalPrize = hackathon.getMoneyPrice();
        double totalAmount = totalPrize.getQuantity();
        int teamSize = winner.getTeamMembersList().size();

        // conversion necessary for precise
        // division and rounding
        BigDecimal amountPerMember = BigDecimal.valueOf(totalAmount)
                .divide(BigDecimal.valueOf(teamSize), 2, RoundingMode.HALF_UP);

        // Simulates redirection to external payment system
        boolean paymentSuccessful = externalPaymentService(user, amountPerMember);

        if (!paymentSuccessful) {
            throw new RuntimeException("Payment failed. Inserted data is not valid");
        }

     }

    // Simulation of external payment service
    private boolean externalPaymentService(User user, BigDecimal amount) {
        return true;
    }
}