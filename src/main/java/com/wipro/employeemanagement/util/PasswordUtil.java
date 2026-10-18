package com.wipro.employeemanagement.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for secure password hashing and verification using BCrypt.
 * Ensures passwords are never stored in plaintext.
 */
public class PasswordUtil {

    private static final int BCRYPT_LOG_ROUNDS = 12;

    private PasswordUtil() {}

    /**
     * Validates if the password meets the assignment rule: exactly 6 characters.
     *
     * @param password Plaintext password to validate
     * @return true if non-null and exactly 6 characters, false otherwise
     */
    public static boolean isValidAssignmentPassword(String password) {
        return password != null && password.length() == 6;
    }

    /**
     * Hashes a plaintext password using BCrypt with a secure salt.
     *
     * @param plainPassword Plaintext password
     * @return BCrypt hashed password string
     * @throws IllegalArgumentException if password is null or blank
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    /**
     * Verifies whether a plaintext candidate password matches the BCrypt hash.
     *
     * @param candidatePassword The plaintext password to check
     * @param hashedPassword    The stored BCrypt hash
     * @return true if candidate password matches, false otherwise
     */
    public static boolean checkPassword(String candidatePassword, String hashedPassword) {
        if (candidatePassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(candidatePassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // In case of invalid hash format
            return false;
        }
    }
}
