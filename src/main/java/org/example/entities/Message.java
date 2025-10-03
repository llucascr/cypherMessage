package org.example.entities;

import org.bson.Document;

import java.time.LocalDate;

public class Message {

    private User to;
    private User from;
    private String title;
    private String message;
    private String token;
    private LocalDate date;

    public Message(User to, User from, String title, String message, String token, LocalDate date) {
        this.to = to;
        this.from = from;
        this.title = title;
        this.message = message;
        this.token = token;
        this.date = date;
    }

    public Document toDocument() {
        return new Document("to", this.to.getName())
                .append("from", this.from.getName())
                .append("title", this.title)
                .append("message", this.message)
                .append("token", this.token)
                .append("date", this.date);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "to=" + to +
                ", from=" + from +
                ", message='" + message;
    }
}
