package com.sunrisedentalclinic.service;

import com.sunrisedentalclinic.dao.UserDAO;
import com.sunrisedentalclinic.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        this.userDAO = new UserDAO();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Authenticates an authorized clinic staff user.
     *
     * The supplied password is compared with the BCrypt
     * password hash stored in the database.
     *
     * @param username username entered by the staff member
     * @param password plain-text password entered by the staff member
     * @return authenticated User object, or null if authentication fails
     */
    public User authenticate(String username, String password) {

        // Validate username
        if (username == null || username.isBlank()) {
            return null;
        }

        // Validate password
        if (password == null || password.isBlank()) {
            return null;
        }

        // Find the user by username
        User user = userDAO.findByUsername(username);

        // User does not exist
        if (user == null) {
            return null;
        }

        // Only active users are allowed to log in
        if (!user.isActive()) {
            return null;
        }

        /*
         * Compare the entered password with the BCrypt
         * password hash stored in the database.
         */
        if (user.getPasswordHash() == null ||
                !passwordEncoder.matches(password, user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    /**
     * Generates a BCrypt password hash.
     *
     * This method can be used when creating or updating
     * staff accounts.
     *
     * @param password plain-text password
     * @return BCrypt password hash
     */
    public String hashPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        return passwordEncoder.encode(password);
    }

    /**
     * Logs the user out.
     *
     * Session/token handling can be added when
     * authentication sessions are implemented.
     */
    public void logout() {
        // Stateless API currently does not maintain a server-side session.
    }
}