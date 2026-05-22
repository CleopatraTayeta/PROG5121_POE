/*package com.chatapp;
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *Unit tests for the Login class.
 * @author Cleopatra Tayeta
 */
public class LoginTest {
    
    public LoginTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    // ── Username Tests (assertEquals) ──────────────────────

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertEquals("Username successfully captured.",
                     // Note: registerUser returns full string;
                     // test checkUserName directly:
                     login.checkUserName() ? 
                     "Username successfully captured." : "fail");
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith",
                                "kyle!!!!!!!", "Ch&&sec@ke99!",
                                "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure "
                   + "that your username contains an underscore and is no "
                   + "more than five characters in length.",
                     login.registerUser());
    }

    // ── Password Tests (assertEquals) ──────────────────────

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertEquals("Username successfully captured.\n"
                   + "Password successfully captured.\n"
                   + "Cell phone number successfully added.",
                     login.registerUser());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "password",
                                "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure "
                   + "that the password contains at least eight characters, "
                   + "a capital letter, a number, and a special character.",
                     login.registerUser());
    }

    // ── Cell Phone Tests (assertEquals) ────────────────────

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertEquals("Username successfully captured.\n"
                   + "Password successfully captured.\n"
                   + "Cell phone number successfully added.",
                     login.registerUser());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "08966553");
        assertEquals("Cell number is incorrectly formatted or does not "
                   + "contain an international code; please correct the "
                   + "number and try again.",
                     login.registerUser());
    }

    // ── Login Tests (assertTrue/assertFalse) ───────────────

    @Test
    public void testLoginSuccessful() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    // ── Boolean Tests (assertTrue/assertFalse) ─────────────

    @Test
    public void testUsernameCorrectlyFormattedBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormattedBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyle!!!!!!!", "Ch&&sec@ke99!",
                                "+27838968976");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "password",
                                "+27838968976");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectBoolean() {
        Login login = new Login("Kyle", "Smith",
                                "kyl_1", "Ch&&sec@ke99!",
                                "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }
}
    

