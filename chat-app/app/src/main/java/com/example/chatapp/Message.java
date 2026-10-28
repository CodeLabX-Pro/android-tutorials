package com.example.chatapp;

/** One chat message, saved at chats/{chatId}/messages/{pushId}. */
public class Message {
    /** The uid of the person who sent it. */
    public String sender;
    public String text;
    /** When it was sent, in milliseconds. The server fills this in, so every phone agrees on the order. */
    public long time;

    public Message() {
    }
}
