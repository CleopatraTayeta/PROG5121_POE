/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Message class handles creating, sending, storing and managing messages.
 * Part 2 of the QuickChat application.
 *
 * @author Cleopatra Tayeta
 */
public final class Message {

    // ── Fields ────────────────────────────────────────────────────────────────

    private final String messageID;       // Auto-generated 10-digit ID
    private final int    messageNumber;   // Num messages sent (counter)
    private final String recipient;       // Recipient cell number
    private final String messageText;     // The actual message
    private final String messageHash;     // Auto-generated hash
    private String flag;            // "Sent", "Stored", or "Disregard"

    // ── Static lists (persist for the whole application run) ─────────────────

    private static ArrayList<String> sentMessages      = new ArrayList<>();
    private static ArrayList<String> messageHashes     = new ArrayList<>();
    private static ArrayList<String> messageIDs        = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Creates a new Message object.
     * @param messageNumber The sequential number of this message
     * @param recipient     The recipient's cell number
     * @param messageText   The body of the message
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    // ── Helper: auto-generate a 10-digit message ID ──────────────────────────

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    // ── 1. checkMessageID ────────────────────────────────────────────────────

    /**
     * Ensures the message ID is not more than 10 characters.
     * @return true if valid, false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // ── 2. checkRecipientCell ────────────────────────────────────────────────

    /**
     * Ensures recipient cell number is no more than 10 characters
     * long and starts with an international code (+27 or similar).
     * Reuses the same logic as Login.checkCellPhoneNumber().
     * @return status message (success or failure)
     */
    public String checkRecipientCell() {
        if (recipient != null
                && recipient.length() <= 13
                && recipient.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not "
             + "contain an international code. Please correct the number "
             + "and try again.";
    }

    // ── 3. createMessageHash ─────────────────────────────────────────────────

    /**
     * Creates a Message Hash in the format:
     * firstTwoDigitsOfID : messageNumber : firstWord LASTWORD
     * All in uppercase.
     * Example: 00:0:HITHANKS
     * @return the generated hash string
     */
    public String createMessageHash() {
        // First two characters of the message ID
        String idPrefix = messageID.substring(0, 2);

        // Split message into words
        String[] words  = messageText.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord  = words[words.length - 1].toUpperCase();

        // Remove any trailing punctuation from lastWord
        lastWord = lastWord.replaceAll("[^A-Z0-9]", "");

        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord)
                .toUpperCase();
    }

    // ── 4. sentMessage ───────────────────────────────────────────────────────

    /**
     * Allows user to Send, Store, or Disregard the message.
     * @param choice 1 = Send, 2 = Disregard, 3 = Store
     * @return status message
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                flag = "Sent";
                sentMessages.add(messageText);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                totalMessagesSent++;
                return "Message successfully sent.";
            }

            case 2 -> {
                flag = "Disregard";
                return "Press 0 to delete the message.";
            }

            case 3 -> {
                flag = "Stored";
                storeMessage();
                return "Message successfully stored.";
            }

            default -> {
                return "Invalid option selected.";
            }
        }
    }

    // ── 5. printMessages ─────────────────────────────────────────────────────

    /**
     * Returns all the messages sent while the program is running.
     * @return formatted string of all sent messages
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Sent Messages ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("Message ").append(i + 1).append(": ")
              .append(sentMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    // ── 6. returnTotalMessages ───────────────────────────────────────────────

    /**
     * Returns the total number of messages sent.
     * @return count of sent messages
     */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // ── 7. storeMessage ──────────────────────────────────────────────────────

    /**
     * Stores message details to a JSON file (messages.json).
     * Uses org.json library.
     *Reference:JSON-Java (2024). org.json. [Online]
     * URL: https://GitHub.com/stleary/JSON-java[Accessed 16 May 2026]
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

    // ── Getters (for display and testing) ────────────────────────────────────

    public String getMessageID()     { return messageID; }
    public String getMessageHash()   { return messageHash; }
    public String getRecipient()     { return recipient; }
    public String getMessageText()   { return messageText; }
    public String getFlag()          { return flag; }
    public int    getMessageNumber() { return messageNumber; }

    /**
     * Returns the full message details in the required display order:
     * Message ID, Message Hash, Recipient, Message
     * @return 
     */
    public String getFullDetails() {
        return "Message ID: "   + messageID   + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: "    + recipient   + "\n"
             + "Message: "      + messageText;
    }
}
 
