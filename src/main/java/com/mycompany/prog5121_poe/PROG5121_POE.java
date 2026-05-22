/*package com.chatapp;
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_poe;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Main application entry point for QuickChat.
 * Integrates Part 1 (Login/Register) with Part 2 (Sending Messages).
 *
 * Flow:
 *  1. User registers (Part 1)
 *  2. User logs in    (Part 1)
 *  3. Welcome to QuickChat menu shown (Part 2)
 *  4. User sends messages via a for-loop until quota reached
 *
 * @author Cleopatra Tayeta
 */
public class PROG5121_POE {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loggedInUser = null;
        boolean loggedIn = false;

        // ─────────────────────────────────────────────────────
        //  PART 1 — Registration & Login
        // ─────────────────────────────────────────────────────

        System.out.println("=== Welcome to My Delulu QuickChat ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            // Registration
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter username (must contain _ and be max 5 chars): ");
            String username = scanner.nextLine();
            System.out.print("Enter password (8+ chars, uppercase, number, special): ");
            String password = scanner.nextLine();
            System.out.print("Enter cell phone number (e.g. +27831234567): ");
            String phone = scanner.nextLine();

            loggedInUser = new Login(firstName, lastName, username, password, phone);
            System.out.println(loggedInUser.registerUser());

        } else if (choice == 3) {
            System.out.println("Goodbye!");
            return;
        }

        // Login (must have a registered user)
        if (loggedInUser != null) {
            System.out.print("\nEnter username to login: ");
            String u = scanner.nextLine();
            System.out.print("Enter password to login: ");
            String p = scanner.nextLine();
            String loginStatus = loggedInUser.returnLoginStatus(u, p);
            System.out.println(loginStatus);
            loggedIn = loginStatus.startsWith("Welcome");
        }

        // Only proceed to Part 2 if login was successful
        if (!loggedIn) {
            System.out.println("Access denied. Please register and login first.");
            scanner.close();
            return;
        }

        // ─────────────────────────────────────────────────────
        //  PART 2 — QuickChat Messaging Menu
        // ─────────────────────────────────────────────────────

        System.out.println("\nWelcome to  My Delulu QuickChat.");

        // Ask user how many messages they want to send
        System.out.print("How many messages do you wish to send? ");
        int numMessages = scanner.nextInt();
        scanner.nextLine();

        boolean running = true;

        while (running) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option (1-3): ");

            int menuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (menuChoice) {

                case 1 -> {
                    // FOR LOOP — iterate for the set number of messages
                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

                        // Get recipient
                        System.out.print("Enter recipient cell number (+27...): ");
                        String recipient = scanner.nextLine();

                        // Get message text
                        System.out.print("Enter message (max 250 chars): ");
                        String text = scanner.nextLine();

                        // Validate message length
                        if (text.length() > 250) {
                            int over = text.length() - 250;
                            System.out.println("Message exceeds 250 characters by "
                                    + over + "; please reduce the size.");
                            i--; // retry this iteration
                            continue;
                        }

                        // Create message object
                        Message msg = new Message(i, recipient, text);

                        // Validate recipient
                        System.out.println(msg.checkRecipientCell());
                        if (!msg.checkRecipientCell()
                                .equals("Cell phone number successfully captured.")) {
                            i--; // retry
                            continue;
                        }

                        // Display generated Message ID
                        System.out.println("Message ID generated: " + msg.getMessageID());

                        // Send / Store / Disregard
                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose (1-3): ");
                        int sendChoice = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println(msg.sentMessage(sendChoice));

                        // Display full details after action
                        System.out.println("\n--- Message Details ---");
                        System.out.println(msg.getFullDetails());
                    }

                    // After all messages processed
                    System.out.println("\nTotal messages sent: "
                            + Message.returnTotalMessages());
                }

                case 2 -> // Coming Soon
                    System.out.println("Coming Soon.");

                case 3 -> {
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                }

                default -> System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }
    private boolean messageID;
    private boolean messageNumber;
    private boolean recipient;
    private boolean messageText;
    private boolean messageHash;
    
     /**
     * Stores message details to a JSON file (messages.json).
     * Uses org.json library.
     *
     */
    public void storeMessage() {
        JSONObject msgObj = new JSONObject();
        msgObj.put("messageID",   messageID);
        msgObj.put("messageNumber", messageNumber);
        msgObj.put("recipient",   recipient);
        msgObj.put("message",     messageText);
        msgObj.put("messageHash", messageHash);
        msgObj.put("flag",        "Stored");

        // Wrap in array for proper JSON structure
        JSONArray array = new JSONArray();
        array.put(msgObj);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(array.toString(4)); // pretty-print with 4-space indent
            file.write("\n");
            System.out.println("Message stored to messages.json");
        } catch (IOException e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
}
}