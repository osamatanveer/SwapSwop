package com.example.swapswop.Classes;

/**
 * This class is used to represent a message. It contains information about the sender, receiver,
 * and the message.
 * @author Süleyman Semih Demir, Muhammed İkbal Doğan
 * @version 1.0, 10 May 2019
 */

public class Chat
{
    // Properties
    private String sender;
    private String receiver;
    private String message;

    /** Constructor for Chat class
     * @param sender the Firebase UID of the sender
     * @param receiver the Firebase UID of the receiver
     */
    public Chat( String sender, String receiver, String message)
    {
        this.sender   = sender;
        this.receiver = receiver;
        this.message  = message;
    }

    /** Empty constructor for Chat class
     */
    public Chat(){

    }

    /** This method returns the id of the sender of the message.
     * @return sender the uid of the message sender
     */
    public String getSender() {
        return sender;
    }

    /** This method sets the id of the sender of the message.
     * @param sender the uid of the sender.
     * @return none.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /** This method returns the id of the receiver of the message.
     * @return receiver the uid of the message receiver.
     */
    public String getReceiver() {
        return receiver;
    }

    /** This method sets the id of the receiver of the message.
     * @param receiver the uid of the message receiver.
     * @return none.
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /** This method returns the message.
     * @return message the message that was sent in the chat.
     */
    public String getMessage() {
        return message;
    }

    /** This method sets the message.
     * @param message the message sent as a String.
     * @return none.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

