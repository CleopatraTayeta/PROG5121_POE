package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 3 — Store Data and Display Task Report.
 * Uses exact POE test data (Messages 1–5).
 *
 * @author Cleopatra Tayeta
 */
public class StoredMessageTest {

    @BeforeEach
    public void setUp() {
        // Clear ALL static arrays before every test
        Message.sentMessages.clear();
        Message.storedMessages.clear();
        Message.disregardMessages.clear();
        Message.sentRecipients.clear();
        Message.storedRecipients.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.storedIDs.clear();
        Message.storedHashes.clear();

        // ── Populate using POE test data ─────────────

        // Message 1 — Sent
        Message m1 = new Message(1, "+27834557896",
                                 "Did you get the cake?");
        m1.sentMessage(1);

        // Message 2 — Stored
        Message m2 = new Message(2, "+27838884567",
            "Where are you? You are late! "
            + "I have asked you to be on time.");
        m2.sentMessage(3);

        // Message 3 — Disregard
        Message m3 = new Message(3, "+27834484567",
                                 "Yohoooo, I am at your gate.");
        m3.sentMessage(2);

        // Message 4 — Sent (note: invalid recipient for cell check)
        Message m4 = new Message(4, "0838884567",
                                 "It is dinner time!");
        m4.sentMessage(1);

        // Message 5 — Stored
        Message m5 = new Message(5, "+27838884567",
                                 "Ok, I am leaving without you.");
        m5.sentMessage(3);
    }

    // ── Test: Sent Messages array populated ───

    @Test
    public void testSentMessagesArrayPopulated() {
        // POE spec: array returns "Did you get the cake?",
        //           "It is dinner time!"
        assertTrue(Message.sentMessages
                          .contains("Did you get the cake?"),
            "Sent array should contain message 1.");
        assertTrue(Message.sentMessages
                          .contains("It is dinner time!"),
            "Sent array should contain message 4.");
    }

    // ── Test: Display longest message ─────────

    @Test
    public void testDisplayLongestMessage() {
        // POE spec: longest = message 2
        assertEquals(
            "Where are you? You are late! "
            + "I have asked you to be on time.",
            Message.getLongestMessage());
    }

    // ── Test: Search for message ID (message 4) ──────────

    @Test
    public void testSearchByMessageID() {
        // Message 4 was sent — get its stored ID
        String id = Message.messageIDs.get(
            Message.sentMessages.indexOf("It is dinner time!"));
        String result = Message.searchByMessageID(id);
        assertEquals("It is dinner time!", result);
    }

    // ── Test: Search all messages for recipient ─────────

    @Test
    public void testSearchByRecipientReturnsCorrectMessages() {
        // POE spec: +27838884567 has messages 2 and 5
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains(
            "Where are you? You are late! "
            + "I have asked you to be on time."),
            "Should find message 2 for that recipient.");
        assertTrue(result.contains(
            "Ok, I am leaving without you."),
            "Should find message 5 for that recipient.");
    }

    // ── Test: Delete message using hash (message 2) ───────────

    @Test
    public void testDeleteMessageByHash() {
        // Get hash of message 2 (first stored message)
        String hashToDelete = Message.storedHashes.get(0);
        String result = Message.deleteMessageByHash(hashToDelete);
        assertTrue(result.contains("successfully deleted"),
            "Deletion should confirm success.");
        assertFalse(Message.storedMessages.contains(
            "Where are you? You are late! "
            + "I have asked you to be on time."),
            "Deleted message should no longer be in array.");
    }

    // ── Test: Display report contains required fields ─────────

    @Test
    public void testDisplayReportContainsRequiredFields() {
        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash:"),
            "Report must show message hash.");
        assertTrue(report.contains("Recipient:"),
            "Report must show recipient.");
        assertTrue(report.contains("Message:"),
            "Report must show message text.");
    }

    // ── Test: Disregarded messages array populated ────────────

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        assertTrue(Message.disregardMessages
                          .contains("Yohoooo, I am at your gate."),
            "Disregard array should contain message 3.");
    }

    // ── Test: Stored messages array populated ─────────────────

    @Test
    public void testStoredMessagesArrayPopulated() {
        assertTrue(Message.storedMessages.contains(
            "Where are you? You are late! "
            + "I have asked you to be on time."),
            "Stored array should contain message 2.");
        assertTrue(Message.storedMessages.contains(
            "Ok, I am leaving without you."),
            "Stored array should contain message 5.");
    }
}
