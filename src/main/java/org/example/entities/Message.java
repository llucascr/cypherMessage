package org.example.entities;

import org.bson.Document;
import org.example.configuration.Criptografia;
import org.example.configuration.MongoHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Message {

    private User to;
    private User from;
    private String title;
    private String message;
    private String token;
    private LocalDate date;

    private Criptografia criptografia = null;

    public Message(){
        this.criptografia = new Criptografia();
    }

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

    public void registerMessage() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Envio de Mensagem");
        scanner.nextLine();
        System.out.print("to: ");
        String to = scanner.nextLine();
        Document userTo = MongoHandler.findUser(to);

        System.out.print("from: ");
        String from = scanner.nextLine();
        Document userFrom = MongoHandler.findUser(from);

        System.out.print("title: ");
        String title = scanner.nextLine();

        System.out.print("message: ");
        String message = scanner.nextLine();

        System.out.print("token: ");
        String token = scanner.nextLine();

        Message msg = new Message(User.toUser(userTo), User.toUser(userFrom), title, message, this.criptografia.encrypt(token), LocalDate.now());

        MongoHandler.insert("message", msg.toDocument());
    }

    public void listMessages() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Lista de Mensagens");
        scanner.nextLine();

        System.out.print("Digite o Token: ");
        String token = scanner.nextLine();

        List<Document> messages = MongoHandler.findAll("message", this.criptografia.encrypt(token));

        if (messages.isEmpty()) throw new Exception("Nunhuma mesagem encontrada");

        // TODO: Implementar um jeito de escolher a mensagem e mostrar mostrar o conteudo dela
        messages.stream()
                .map(doc -> String.format("to=%s, from=%s, title=%s",
                        doc.getString("to"),
                        doc.getString("from"),
                        doc.getString("title")))
                .forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "to=" + to +
                ", from=" + from +
                ", message='" + message;
    }
}
