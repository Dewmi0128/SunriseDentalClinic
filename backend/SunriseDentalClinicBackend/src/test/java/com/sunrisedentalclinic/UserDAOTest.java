package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.UserDAO;
import com.sunrisedentalclinic.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest {

    @Test
    void testFindByUsername() {

        UserDAO userDAO = new UserDAO();

        User user = userDAO.findByUsername("admin");

        assertNotNull(user, "Admin user should exist");
        assertEquals("admin", user.getUsername());
        assertEquals("STAFF", user.getRole());
        assertTrue(user.isActive());
    }
}