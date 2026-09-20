package com.sunrisedentalclinic;

import com.sunrisedentalclinic.util.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DatabaseConnectionTest {

    @Test
    void testDatabaseConnection() throws Exception {

        Connection connection = DatabaseConnection.getConnection();

        assertNotNull(connection, "Database connection should not be null");
        assertFalse(connection.isClosed(), "Database connection should be open");

        connection.close();
    }
}