package org.example.entities;

public class Message {

    private User to;
    private User from;
    private String message;
    private String token;

    public Message(User to, User from, String message, String token) {
        this.to = to;
        this.from = from;
        this.message = message;
        this.token = token;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
