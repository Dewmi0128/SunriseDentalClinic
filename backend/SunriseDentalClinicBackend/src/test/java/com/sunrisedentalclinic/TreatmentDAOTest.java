package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.TreatmentDAO;
import com.sunrisedentalclinic.model.Treatment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreatmentDAOTest {

    @Test
    void findByIdShouldReturnExistingTreatment() {

        TreatmentDAO treatmentDAO = new TreatmentDAO();

        Treatment treatment = treatmentDAO.findById(1);

        assertNotNull(treatment);
        assertEquals(1, treatment.getTreatmentId());
        assertEquals("Dental Cleaning", treatment.getTreatmentName());
        assertEquals(3500.00, treatment.getCost());
    }

    @Test
    void findByIdShouldReturnNullForUnknownTreatment() {

        TreatmentDAO treatmentDAO = new TreatmentDAO();

        Treatment treatment = treatmentDAO.findById(99999);

        assertNull(treatment);
    }
}