package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.DentistDAO;
import com.sunrisedentalclinic.model.Dentist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DentistDAOTest {

    @Test
    void findByIdShouldReturnExistingDentist() {

        DentistDAO dentistDAO = new DentistDAO();

        Dentist dentist = dentistDAO.findById(1);

        assertNotNull(dentist);
        assertEquals(1, dentist.getDentistId());
        assertEquals("Dr. Nimal Perera", dentist.getFullName());
    }

    @Test
    void findByIdShouldReturnNullForUnknownDentist() {

        DentistDAO dentistDAO = new DentistDAO();

        Dentist dentist = dentistDAO.findById(99999);

        assertNull(dentist);
    }
}