package com.sunrisedentalclinic.service;

import com.sunrisedentalclinic.dao.UserDAO;
import com.sunrisedentalclinic.model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticates a clinic staff user.
     *
     * @param username username entered by the user
     * @param password password entered by the user
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

        // Find user from database
        User user = userDAO.findByUsername(username);

        // User does not exist
        if (user == null) {
            return null;
        }

        // User account is inactive
        if (!user.isActive()) {
            return null;
        }

        /*
         * Current development database stores the password value
         * directly in the password_hash field.
         *
         * Password hashing will be added during the security
         * implementation stage.
         */
        if (!user.getPasswordHash().equals(password)) {
            return null;
        }

        return user;
    }

    /**
     * Logs the user out.
     *
     * Session handling will be implemented when the web layer
     * is added.
     */
    public void logout() {
        // Web-session handling will be implemented later.
    }
}