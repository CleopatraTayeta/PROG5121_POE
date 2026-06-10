/*package com.chatapp;
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;

/**
 *Login class handles user registration and authentication.
 * @author Cleopatra Tayeta
 */
public class Login {
    
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Constructor
    public Login(String firstName, String lastName,
                 String username, String password,
                 String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    /**
     * Checks username contains underscore and is max 5 chars.
     * @return true if valid, false otherwise
     */
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks password complexity:
     * - At least 8 characters
     * - Contains capital letter
     * - Contains number
     * - Contains special character
     * @return true if valid, false otherwise
     */
    public boolean checkPasswordComplexity() {
        if (password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUpper && hasDigit && hasSpecial;
    }

    /**
     * Validates South African cell phone number using regex.
     * Must contain international code (+27) followed by
     * no more than 10 digits.
     *
     * Regex pattern adapted from:
     * Author: Stack Overflow (2015). Regex for South African phone numbers. [Online]  
     * URL:https://stackoverflow.com/
     * [Accessed 18 April 2026].
     * @return true if valid, false otherwise
     */
    public boolean checkCellPhoneNumber() {
        // Regex: starts with +27, followed by 9 digits (total 10 after code)
        return cellPhoneNumber.matches("^\\+27[0-9]{9}$");
    }

    /**
     * Registers user and returns appropriate status message.
     * @return registration status string
     */
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure "
                 + "that your username contains an underscore and is no "
                 + "more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure "
                 + "that the password contains at least eight characters, "
                 + "a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not "
                 + "contain an international code; please correct the "
                 + "number and try again.";
        }
        return "Username successfully captured.\n"
             + "Password successfully captured.\n"
             + "Cell phone number successfully added.";
    }

    /**
     * Verifies login credentials match registered details.
     * @param enteredUsername username entered at login
     * @param enteredPassword password entered at login
     * @return true if credentials match, false otherwise
     */
    public boolean loginUser(String enteredUsername,
                             String enteredPassword) {
        return this.username.equals(enteredUsername)
            && this.password.equals(enteredPassword);
    }

    /**
     * Returns login status message.
     * @param enteredUsername username entered
     * @param enteredPassword password entered
     * @return welcome or error message
     */
    public String returnLoginStatus(String enteredUsername,
                                    String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName
                 + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
    
