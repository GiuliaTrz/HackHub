package com.project.hackhub.handler;

import com.project.hackhub.model.hackathon.Hackathon;
import com.project.hackhub.model.hackathon.Soldi;
import com.project.hackhub.model.hackathon.state.HackathonStateType;
import com.project.hackhub.model.team.Team;
import com.project.hackhub.model.utente.UtenteRegistrato;
import com.project.hackhub.repository.HackathonRepository;
import com.project.hackhub.repository.UtenteRegistratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrizeHandler {

    private final HackathonRepository hackathonRepository;
    private final UtenteRegistratoRepository utenteRepository;

    /**
     * Riscuote il premio in denaro per un membro del team vincitore.
     * Il premio viene diviso equamente tra i membri del team.
     * Simula il reindirizzamento a un sistema di pagamento esterno.
     *
     * @param userId      ID del membro del team che richiede la riscossione
     * @param hackathonId ID dell'hackathon
     * @throws IllegalStateException se l'hackathon non ha un vincitore o il premio è già stato riscosso
     * @author Giulia Trozzi
     */
    public void claimPrize(UUID userId, UUID hackathonId) {
        UtenteRegistrato user = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        // Verifica che l'hackathon sia terminato, e quindi nello stato "CONCLUSO"
        if (hackathon.getState().getStateType() != HackathonStateType.CONCLUSO) {
            throw new IllegalStateException("L'hackathon non è ancora terminato");
        }

        Team winner = hackathon.getWinner();
        if (winner == null) {
            throw new IllegalStateException("Nessun team vincitore proclamato per questo hackathon");
        }

        if (!winner.getTeamMembersList().contains(user)) {
            throw new UnsupportedOperationException("Solo i membri del team vincitore possono riscuotere il premio");
        }

        Soldi totalPrize = hackathon.getMoneyPrice();
        double totalAmount = totalPrize.getQuantity();   // <-- Usa il getter corretto
        int teamSize = winner.getTeamMembersList().size();

        // conversione necessaria per avere una
        // divisione precisa e l' arrotondamento
        BigDecimal amountPerMember = BigDecimal.valueOf(totalAmount)
                .divide(BigDecimal.valueOf(teamSize), 2, RoundingMode.HALF_UP);

        // Simula il reindirizzamento al sistema di pagamento esterno
        boolean paymentSuccessful = externalPaymentService(user, amountPerMember);

        if (!paymentSuccessful) {
            throw new RuntimeException("Pagamento fallito. I dati inseriti non sono validi");
        }

    }

    // Simulazione del servizio di pagamento esterno
    private boolean externalPaymentService(UtenteRegistrato user, BigDecimal amount) {
        // Qui si integrerebbe con un gateway di pagamento reale (estendibilità)
        return true;
    }
}