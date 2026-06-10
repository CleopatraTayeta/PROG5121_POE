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
 * Integrates Part 1 (Login/Register), Part 2 (Send Messages),
 * and Part 3 (Store Data and Display Task Report).
 *
 * @author Cleopatra Tayeta
 * @version 3.0
 */
public class PROG5121_POE {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Login loggedInUser = null;
        boolean loggedIn   = false;

        // ═══════════════════════════════════════════════
        //  PART 1 — Registration & Login
        // ═══════════════════════════════════════════════

         System.out.println("=== Welcome to My Delulu QuickChat ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter username "
                           + "(must contain _ and be max 5 chars): ");
            String username = scanner.nextLine();
            System.out.print("Enter password "
                           + "(8+ chars, uppercase, number, special): ");
            String password = scanner.nextLine();
            System.out.print("Enter cell phone number "
                           + "(e.g. +27831234567): ");
            String phone = scanner.nextLine();

            loggedInUser = new Login(firstName, lastName,
                                     username, password, phone);
            System.out.println(loggedInUser.registerUser());

        } else {
            System.out.println("Goodbye!");
            scanner.close();
            return;
        }

        // ── Login ──────────────────────────────────────
        if (loggedInUser != null) {
            System.out.print("\nEnter username to login: ");
            String u = scanner.nextLine();
            System.out.print("Enter password to login: ");
            String p = scanner.nextLine();
            String loginStatus = loggedInUser.returnLoginStatus(u, p);
            System.out.println(loginStatus);
            loggedIn = loginStatus.startsWith("Welcome");
        }

        if (!loggedIn) {
            System.out.println(
                "Access denied. Please register and login first.");
            scanner.close();
            return;
        }

        // ═══════════════════════════════════════════════
        //  PART 2 — QuickChat Messaging
        // ═══════════════════════════════════════════════

        System.out.println("\nWelcome to My Delulu QuickChat");
        System.out.print("How many messages do you wish to send? ");
        int numMessages = scanner.nextInt();
        scanner.nextLine();

        boolean running = true;

        while (running) {

            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");     // PART 3
            System.out.print("Choose an option (1-4): ");

            int menuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (menuChoice) {

                // ── OPTION 1: Send Messages ─────────────
                case 1 -> {
                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message "
                            + i + " of " + numMessages + " ---");

                        System.out.print(
                            "Enter recipient cell number (+27...): ");
                        String recipient = scanner.nextLine();

                        System.out.print(
                            "Enter message (max 250 chars): ");
                        String text = scanner.nextLine();

                        // Validate length
                        if (text.length() > 250) {
                            int over = text.length() - 250;
                            System.out.println(
                                "Message exceeds 250 characters by "
                                + over + "; please reduce the size.");
                            i--;
                            continue;
                        }

                        Message msg = new Message(i, recipient, text);

                        // Validate recipient
                        String cellCheck = msg.checkRecipientCell();
                        System.out.println(cellCheck);
                        if (!cellCheck.equals(
                                "Cell phone number successfully captured.")) {
                            i--;
                            continue;
                        }

                        System.out.println("Message ID generated: "
                            + msg.getMessageID());
                        System.out.println("Message Hash: "
                            + msg.getMessageHash());

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose (1-3): ");
                        int sendChoice = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println(msg.sentMessage(sendChoice));
                        System.out.println("\n--- Message Details ---");
                        System.out.println(msg.getFullDetails());
                    }

                    System.out.println("\nTotal messages sent: "
                        + Message.returnTotalMessages());
                }

                // ── OPTION 2: Recently Sent ─────────────
                case 2 -> System.out.println("Coming Soon.");

                // ── OPTION 3: Quit ──────────────────────
                case 3 -> {
                    running = false;
                    System.out.println(
                        "Thank you for using QuickChat. Goodbye!");
                }

                // ═══════════════════════════════════════════════
                //  PART 3 — Stored Messages Menu
                // ═══════════════════════════════════════════════
                case 4 -> {

                    // Load JSON into storedMessages array first
                    Message.readStoredMessagesFromJSON();

                    boolean storedMenuRunning = true;

                    while (storedMenuRunning) {
                        System.out.println(
                            "\n--- Stored Messages Menu ---");
                        System.out.println(
                            "a) Display all stored messages "
                            + "(sender & recipient)");
                        System.out.println(
                            "b) Display longest stored message");
                        System.out.println(
                            "c) Search for a message ID");
                        System.out.println(
                            "d) Search by recipient");
                        System.out.println(
                            "e) Delete a message by hash");
                        System.out.println(
                            "f) Display full message report");
                        System.out.println(
                            "x) Back to main menu");
                        System.out.print("Choose: ");
                        String sub = scanner.nextLine().trim()
                                            .toLowerCase();

                        switch (sub) {

                            // a) Display all stored messages
                            case "a" -> {
                                if (Message.storedMessages.isEmpty()) {
                                    System.out.println(
                                        "No stored messages.");
                                } else {
                                    for (int i = 0;
                                         i < Message.storedMessages
                                                     .size(); i++) {
                                        System.out.println(
                                            "Recipient: "
                                            + (i < Message.storedRecipients
                                                         .size()
                                               ? Message.storedRecipients
                                                        .get(i)
                                               : "N/A")
                                            + " | Message: "
                                            + Message.storedMessages
                                                     .get(i));
                                    }
                                }
                            }

                            // b) Longest message
                            case "b" -> System.out.println(
                                "Longest Message:\n"
                                + Message.getLongestMessage());

                            // c) Search by message ID
                            case "c" -> {
                                System.out.print(
                                    "Enter Message ID to search: ");
                                String searchID = scanner.nextLine();
                                System.out.println(
                                    Message.searchByMessageID(searchID));
                            }

                            // d) Search by recipient
                            case "d" -> {
                                System.out.print(
                                    "Enter recipient number: ");
                                String recip = scanner.nextLine();
                                System.out.println(
                                    Message.searchByRecipient(recip));
                            }

                            // e) Delete by hash
                            case "e" -> {
                                System.out.print(
                                    "Enter message hash to delete: ");
                                String hash = scanner.nextLine();
                                System.out.println(
                                    Message.deleteMessageByHash(hash));
                            }

                            // f) Display full report
                            case "f" -> System.out.println(
                                Message.displayReport());

                            // x) Back
                            case "x" -> storedMenuRunning = false;

                            default -> System.out.println(
                                "Invalid option.");
                        }
                    }
                }

                default -> System.out.println(
                    "Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }

        scanner.close();
    }
}