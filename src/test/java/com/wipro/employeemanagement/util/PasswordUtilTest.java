package com.wipro.employeemanagement.util;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for password hashing, 6-character length rule, and regex validations.
 */
public class PasswordUtilTest {

    private static final Pattern EMP_NO_PATTERN = Pattern.compile("^[A-Za-z][0-9]{4}$");

    @Test
    public void testPasswordSixCharactersRule() {
        // Must be exactly 6 characters
        assertTrue(PasswordUtil.isValidAssignmentPassword("123456"));
        assertTrue(PasswordUtil.isValidAssignmentPassword("passwo"));
        assertTrue(PasswordUtil.isValidAssignmentPassword("A1b2C3"));

        // Invalid lengths
        assertFalse(PasswordUtil.isValidAssignmentPassword(null));
        assertFalse(PasswordUtil.isValidAssignmentPassword(""));
        assertFalse(PasswordUtil.isValidAssignmentPassword("12345"));   // 5 chars
        assertFalse(PasswordUtil.isValidAssignmentPassword("1234567")); // 7 chars
    }

    @Test
    public void testPasswordHashingAndVerification() {
        String plain = "secr6t";
        String hashed = PasswordUtil.hashPassword(plain);

        assertNotNull(hashed);
        assertNotEquals(plain, hashed);
        assertTrue(hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$"));

        // Successful match
        assertTrue(PasswordUtil.checkPassword(plain, hashed));

        // Unsuccessful match
        assertFalse(PasswordUtil.checkPassword("wrong1", hashed));
        assertFalse(PasswordUtil.checkPassword(null, hashed));
    }

    @Test
    public void testEmployeeNumberPattern() {
        // Valid patterns: 1 letter followed by exactly 4 digits
        assertTrue(EMP_NO_PATTERN.matcher("A1234").matches());
        assertTrue(EMP_NO_PATTERN.matcher("e9876").matches());
        assertTrue(EMP_NO_PATTERN.matcher("Z0001").matches());

        // Invalid patterns
        assertFalse(EMP_NO_PATTERN.matcher("12345").matches());  // starts with digit
        assertFalse(EMP_NO_PATTERN.matcher("AB123").matches());  // 2 letters
        assertFalse(EMP_NO_PATTERN.matcher("A123").matches());   // only 3 digits
        assertFalse(EMP_NO_PATTERN.matcher("A12345").matches()); // 5 digits
        assertFalse(EMP_NO_PATTERN.matcher("A123a").matches());  // contains trailing letter
    }
}
