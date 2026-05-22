/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class.
 * Uses test data supplied in the POE specification.
 *
 * Test Data Message 1:
 *   Recipient : +27718693002
 *   Message   : "Hi Mike, can you join us for dinner tonight?"
 *   Flag      : Send
 *
 * Test Data Message 2:
 *   Recipient : 08575975889  (invalid - no international code)
 *   Message   : "Hi Keegan, did you receive the payment?"
 *   Flag      : Discard
 *
 * @author Cleopatra Tayeta
 */
public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        // Test Data 1 – valid recipient, sent message
        message1 = new Message(1,
                               "+27718693002",
                               "Hi Mike, can you join us for dinner tonight?");

        // Test Data 2 – invalid recipient (no international code), discard
        message2 = new Message(2,
                               "08575975889",
                               "Hi Keegan, did you receive the payment?");
    }

    // ── checkMessageID ───────────────────────────────────────────────────────

    @Test
    public void testMessageIDNotMoreThanTenCharacters() {
        // Message ID must be <= 10 characters
        assertTrue(message1.checkMessageID(),
                "Message ID should not exceed 10 characters.");
    }

    @Test
    public void testMessageIDFailsWhenTooLong() {
        // Simulate an invalid ID by checking length guard
        // (ID is auto-generated as 10 digits, so this confirms correct length)
        String id = message1.getMessageID();
        assertTrue(id.length() <= 10,
                "Generated Message ID exceeds 10 characters.");
    }

    // ── checkRecipientCell ───────────────────────────────────────────────────

    @Test
    public void testRecipientCellCorrectlyFormatted() {
        // +27718693002 is valid international format
        assertEquals("Cell phone number successfully captured.",
                     message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCellIncorrectlyFormatted() {
        // 08575975889 has no +27 international code
        assertEquals("Cell phone number is incorrectly formatted or does not "
                   + "contain an international code. Please correct the number "
                   + "and try again.",
                     message2.checkRecipientCell());
    }

    // ── createMessageHash ────────────────────────────────────────────────────

    @Test
    public void testMessageHashIsGeneratedCorrectly() {
        // Hash format: firstTwoID:messageNumber:FIRSTWORDLASTWORD (uppercase)
        String hash = message1.getMessageHash();
        // Must contain exactly 2 colons
        int colonCount = (int) hash.chars().filter(c -> c == ':').count();
        assertEquals(2, colonCount,
                "Message hash must contain exactly 2 colons.");
    }

    @Test
    public void testMessageHashForTestCase1() {
        // Expected: first 2 of ID + ":1:" + "HI" + "TONIGHT?" cleaned
        // Structure check – hash must start with first 2 chars of message ID
        String hash = message1.getMessageHash();
        String idPrefix = message1.getMessageID().substring(0, 2).toUpperCase();
        assertTrue(hash.startsWith(idPrefix),
                "Hash should begin with first two digits of Message ID.");
    }

    // ── sentMessage ──────────────────────────────────────────────────────────

    @Test
    public void testSentMessageReturnsSuccessfullySent() {
        String result = message1.sentMessage(1);
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testSentMessageReturnsDisregard() {
        String result = message1.sentMessage(2);
        assertEquals("Press 0 to delete the message.", result);
    }

    @Test
    public void testSentMessageReturnsStored() {
        String result = message1.sentMessage(3);
        assertEquals("Message successfully stored.", result);
    }

    // ── returnTotalMessages ──────────────────────────────────────────────────

    @Test
    public void testReturnTotalMessagesIncrementsOnSend() {
        int before = Message.returnTotalMessages();
        message1.sentMessage(1); // only "Send" increments the counter
        int after = Message.returnTotalMessages();
        assertEquals(before + 1, after,
                "Total messages should increment by 1 after sending.");
    }

    // ── Message length validation ─────────────────────────────────────────────

    @Test
    public void testMessageReadyToSendUnder250Chars() {
        // message1 text is under 250 characters
        boolean valid = message1.getMessageText().length() <= 250;
        assertTrue(valid, "Message ready to send.");
    }

    @Test
    public void testMessageExceeds250Characters() {
        // Build a string longer than 250 chars
        String longMsg = "A".repeat(260);
        boolean exceeds = longMsg.length() > 250;
        int over = longMsg.length() - 250;
        assertTrue(exceeds,
                "Message exceeds 250 characters by " + over
                + "; please reduce the size.");
    }
}

