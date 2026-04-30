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

        // Verify that the hackathon is finished, and therefore in "CONCLUDED" state
        if (hackathon.getState().getStateType() != HackathonStateType.CONCLUDED) {
             throw new IllegalStateException("The hackathon is not yet finished");
         }

         Team winner = hackathon.getWinner();
         if (winner == null) {
             throw new IllegalStateException("No winning team declared for this hackathon");
         }

         if (!winner.getTeamMembersList().contains(user)) {
             throw new UnsupportedOperationException("Only members of the winning team can claim the prize");
         }

         Money totalPrize = hackathon.getMoneyPrice();
         double totalAmount = totalPrize.getQuantity();   // <-- Uses the correct getter
         int teamSize = winner.getTeamMembersList().size();

         // conversion necessary to have a
         // precise division and rounding
         BigDecimal amountPerMember = BigDecimal.valueOf(totalAmount)
                 .divide(BigDecimal.valueOf(teamSize), 2, RoundingMode.HALF_UP);

         // Simulates redirection to the external payment system
         boolean paymentSuccessful = externalPaymentService(user, amountPerMember);

         if (!paymentSuccessful) {
             throw new RuntimeException("Payment failed. The entered data is not valid");
         }

     }

    // Simulation of the external payment service
    private boolean externalPaymentService(User user, BigDecimal amount) {
         // Here you would integrate with a real payment gateway (extensibility)
         return true;
     }
}