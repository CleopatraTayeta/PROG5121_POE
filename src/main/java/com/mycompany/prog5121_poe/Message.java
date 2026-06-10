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

    public static ArrayList<String> disregardMessages = new ArrayList<>();
    public static ArrayList<String> sentRecipients    = new ArrayList<>();
    public static ArrayList<String> storedRecipients  = new ArrayList<>();
    public static ArrayList<String> storedMessages    = new ArrayList<>();
    public static ArrayList<String> storedIDs         = new ArrayList<>();
    public static ArrayList<String> storedHashes      = new ArrayList<>();
    public static ArrayList<String> sentMessages      = new ArrayList<>();
    public static ArrayList<String> messageHashes     = new ArrayList<>();
    public static ArrayList<String> messageIDs        = new ArrayList<>();
    public static int totalMessagesSent = 0;

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
    sentRecipients.add(recipient); 
    messageHashes.add(messageHash);
    messageIDs.add(messageID);
    totalMessagesSent++;
    return "Message successfully sent.";
}

case 2 -> {
    flag = "Disregard";
    disregardMessages.add(messageText); 
    return "Press 0 to delete the message.";
}

case 3 -> {
    flag = "Stored";
    storedMessages.add(messageText);
    storedRecipients.add(recipient);
    storedIDs.add(messageID);
    storedHashes.add(messageHash);
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
    // 
// PART 3 METHOD 1: getLongestMessage()
// Finds and returns the longest message from sent + stored
// 
public static String getLongestMessage() {
    ArrayList<String> all = new ArrayList<>();
    all.addAll(sentMessages);
    all.addAll(storedMessages);

    if (all.isEmpty()) return "No messages available.";

    String longest = "";
    for (String msg : all) {
        if (msg.length() > longest.length()) {
            longest = msg;
        }
    }
    return longest;
}

// 
// PART 3 METHOD 2: searchByMessageID()
// Searches all messageIDs + storedIDs for a match
// Returns the corresponding message text
//
public static String searchByMessageID(String searchID) {
    // Search sent messages first
    for (int i = 0; i < messageIDs.size(); i++) {
        if (messageIDs.get(i).equals(searchID)) {
            return sentMessages.get(i);
        }
    }
    // Then search stored messages
    for (int i = 0; i < storedIDs.size(); i++) {
        if (storedIDs.get(i).equals(searchID)) {
            return storedMessages.get(i);
        }
    }
    return "Message ID not found.";
}

// 
// PART 3 METHOD 3: searchByRecipient()
// Returns all messages (sent + stored) for a given recipient
//
public static String searchByRecipient(String recipientNum) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < sentRecipients.size(); i++) {
        if (sentRecipients.get(i).equals(recipientNum)) {
            sb.append(sentMessages.get(i)).append("\n");
        }
    }
    for (int i = 0; i < storedRecipients.size(); i++) {
        if (storedRecipients.get(i).equals(recipientNum)) {
            sb.append(storedMessages.get(i)).append("\n");
        }
    }
    return sb.length() == 0
           ? "No messages found for that recipient."
           : sb.toString().trim();
}

// 
// PART 3 METHOD 4: deleteMessageByHash()
// Deletes a message from sent or stored using its hash
// 
public static String deleteMessageByHash(String hash) {
    // Search sent messages
    for (int i = 0; i < messageHashes.size(); i++) {
        if (messageHashes.get(i).equals(hash)) {
            String deleted = sentMessages.remove(i);
            sentRecipients.remove(i);
            messageHashes.remove(i);
            messageIDs.remove(i);
            return "Message: \"" + deleted + "\" successfully deleted.";
        }
    }
    // Search stored messages
    for (int i = 0; i < storedHashes.size(); i++) {
        if (storedHashes.get(i).equals(hash)) {
            String deleted = storedMessages.remove(i);
            storedRecipients.remove(i);
            storedHashes.remove(i);
            storedIDs.remove(i);
            return "Message: \"" + deleted + "\" successfully deleted.";
        }
    }
    return "Hash not found. No message deleted.";
}

//
// PART 3 METHOD 5: displayReport()
// Displays full report of all sent messages:
// Message Hash, Recipient, Message
// 
public static String displayReport() {
    if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
        return "No messages to report.";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("========== MESSAGE REPORT ==========\n");

    for (int i = 0; i < sentMessages.size(); i++) {
        sb.append("--- Sent Message ").append(i + 1).append(" ---\n");
        sb.append("Message Hash: ")
          .append(i < messageHashes.size()
                  ? messageHashes.get(i) : "N/A").append("\n");
        sb.append("Recipient:    ")
          .append(i < sentRecipients.size()
                  ? sentRecipients.get(i) : "N/A").append("\n");
        sb.append("Message:      ")
          .append(sentMessages.get(i)).append("\n");
    }

    for (int i = 0; i < storedMessages.size(); i++) {
        sb.append("--- Stored Message ").append(i + 1).append(" ---\n");
        sb.append("Message Hash: ")
          .append(i < storedHashes.size()
                  ? storedHashes.get(i) : "N/A").append("\n");
        sb.append("Recipient:    ")
          .append(i < storedRecipients.size()
                  ? storedRecipients.get(i) : "N/A").append("\n");
        sb.append("Message:      ")
          .append(storedMessages.get(i)).append("\n");
    }

    sb.append("=====================================\n");
    return sb.toString();
}

// 
// PART 3 METHOD 6: readStoredMessagesFromJSON()
// Reads messages.json back into storedMessages array
//
// Reference:
// JSON-Java (2024). org.json. [Online]
// URL: https://github.com/stleary/JSON-java
// [Accessed 16 May 2026]
// 
public static void readStoredMessagesFromJSON() {
    try {
        // Read entire file content
        java.nio.file.Path path =
            java.nio.file.Paths.get("messages.json");

        if (!java.nio.file.Files.exists(path)) {
            System.out.println("No stored messages file found.");
            return;
        }

        String content = new String(
            java.nio.file.Files.readAllBytes(path));

        // File may contain multiple JSON arrays appended
        // Parse each array block
        content = content.trim();
        if (content.isEmpty()) return;

        // Wrap all content in outer array if needed
        if (!content.startsWith("[")) {
            content = "[" + content + "]";
        }

        // Handle multiple arrays written on separate lines
        content = content.replace("]\n[", ",")
                         .replace("]\r\n[", ",");

        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String msg  = obj.optString("message", "");
            String id   = obj.optString("messageID", "");
            String hash = obj.optString("messageHash", "");
            String rec  = obj.optString("recipient", "");

            if (!msg.isEmpty()) {
                storedMessages.add(msg);
                storedIDs.add(id);
                storedHashes.add(hash);
                storedRecipients.add(rec);
            }
        }
        System.out.println("Stored messages loaded from JSON.");

    } catch (Exception e) {
        System.out.println("Error reading JSON: " + e.getMessage());
    }
}
}
 
