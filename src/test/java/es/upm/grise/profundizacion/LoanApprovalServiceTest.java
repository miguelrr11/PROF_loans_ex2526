package es.upm.grise.profundizacion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.upm.grise.profundizacion.LoanApprovalService.Applicant;
import es.upm.grise.profundizacion.LoanApprovalService.Decision;

public class LoanApprovalServiceTest {

    private final LoanApprovalService service = new LoanApprovalService();

    
    // camino 1, resultado esperado: REJECTED
    @Test
    void caminoBasico1_scoreMenor500_rejected() {
        // score = 400 (< 500), income = 3000, hasDefaults = false, isVip = false
        Applicant applicant = new Applicant(3000, 400, false, false);
        int amountRequested = 10000;
        int termMonths = 12;

        Decision result = service.evaluateLoan(applicant, amountRequested, termMonths);

        assertEquals(Decision.REJECTED, result);
    }

   
    // camino 2, resultado esperado: APPROVED
    @Test
    void caminoBasico2_scoreMedio_ingresoAlto_sinImpagos_vip_approved() {
        // score = 620 (>= 500, < 650, >= 600), income = 3000 (>= 2500),
        // hasDefaults = false, isVip = true
        Applicant applicant = new Applicant(3000, 620, false, true);
        int amountRequested = 10000;
        int termMonths = 12;

        Decision result = service.evaluateLoan(applicant, amountRequested, termMonths);

        assertEquals(Decision.APPROVED, result);
    }

   
    // camino 3, resultado esperado: REJECTED
    @Test
    void caminoBasico3_scoreMedio_noCumpleRequisitos_rejected() {
        // score = 550 (>= 500, < 650), income = 2000 (< 2500),
        // hasDefaults = false, isVip = false
        Applicant applicant = new Applicant(2000, 550, false, false);
        int amountRequested = 10000;
        int termMonths = 12;

        Decision result = service.evaluateLoan(applicant, amountRequested, termMonths);

        assertEquals(Decision.REJECTED, result);
    }

   
    // camino 4, resultado esperado: APPROVED
    @Test
    void caminoBasico4_scoreAlto_montoAceptable_approved() {
        // score = 700 (>= 650), income = 3000, amount = 20000 (<= 3000*8 = 24000)
        // hasDefaults = false, isVip = false
        Applicant applicant = new Applicant(3000, 700, false, false);
        int amountRequested = 20000;
        int termMonths = 12;

        Decision result = service.evaluateLoan(applicant, amountRequested, termMonths);

        assertEquals(Decision.APPROVED, result);
    }


    // camino 5, resultado esperado: MANUAL_REVIEW
    @Test
    void caminoBasico5_scoreAlto_montoExcesivo_manualReview() {
        // score = 700 (>= 650), income = 3000, amount = 30000 (> 3000*8 = 24000)
        // hasDefaults = false, isVip = false (no cumple condiciones VIP)
        Applicant applicant = new Applicant(3000, 700, false, false);
        int amountRequested = 30000;
        int termMonths = 12;

        Decision result = service.evaluateLoan(applicant, amountRequested, termMonths);

        assertEquals(Decision.MANUAL_REVIEW, result);
    }
}
