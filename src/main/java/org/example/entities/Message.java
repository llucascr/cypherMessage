package org.example.entities;

import org.bson.Document;
import org.example.configuration.Criptografia;
import org.example.configuration.MongoHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Message {

    private User to;
    private User from;
    private String title;
    private String message;
    private LocalDate date;
    private String status = "não lida";

    public Message() {}

    public Message(User to, User from, String title, String message, LocalDate date, String status) {
        this.to = to;
        this.from = from;
        this.title = title;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public Document toDocument() {
        return new Document("to", this.to.getName())
                .append("from", this.from.getName())
                .append("title", this.title)
                .append("message", this.message)
                .append("date", this.date)
                .append("status", this.status);
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

    public List<Document> findAllMessage(Document filter, String token) throws Exception {
        List<Document> results = new ArrayList<>();
        List<Document> all = MongoHandler.findAll("message", filter);

        for (Document doc : all) {
            try {
                String encryptedMessage = doc.getString("message");
                String decrypted = Criptografia.decrypt(encryptedMessage, token);

                Document copy = new Document(doc);
                copy.put("message", decrypted);
                results.add(copy);

            } catch (Exception e) {
                throw new Exception("Não foi possivel buscar mensagens");
            }
        }

        return results;
    }

    public void registerMessage() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Envio de Mensagem");
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

        Message msg = new Message(User.toUser(userTo), User.toUser(userFrom), title, Criptografia.encrypt(message, token), LocalDate.now(), this.status);

        MongoHandler.insert("message", msg.toDocument());
    }

    public void updateStatus(Object id, String status) {

        Document filter = new Document("_id", id);
        Document update = new Document("$set", new Document("status", status)
                .append("date_read", new Date()));

        MongoHandler.update("message", filter, update);
    }

    public void listMessages() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Lista de Mensagens");

        System.out.print("Digite o Token: ");
        String token = scanner.nextLine();

        try {
            mensagem(0, token);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    private void mensagem(int choice, String token) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Document filter = new Document("status", "não lida");
        List<Document> messages = this.findAllMessage(filter, token);

        // Caso base da recursão
        if (messages.isEmpty()) {
            System.out.println("\nNenhuma mensagem não lida encontrada.\n");
            return;
        }

        System.out.println("\n=== MENSAGENS NÃO LIDAS ===");
        for (int i = 0; i < messages.size(); i++) {
            Document doc = messages.get(i);
            System.out.printf("%d) %s - %s%n", i + 1, doc.getString("from"), doc.getString("title"));
        }
        System.out.println("0) Sair");

        System.out.print("\nEscolha uma mensagem: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) {
            System.out.println("\nSaindo...");
            return;
        }

        try {
            Document chosen = messages.get(choice - 1);

            System.out.printf(
                    "\n--- Mensagem ---\nDe: %s\nPara: %s\nTítulo: %s\nConteúdo: %s%n",
                    chosen.getString("from"),
                    chosen.getString("to"),
                    chosen.getString("title"),
                    chosen.getString("message")
            );

            // Atualiza status da mensagem
            this.updateStatus(chosen.getObjectId("_id"), "lida");
            System.out.println("\nMensagem marcada como lida!\n");

            mensagem(choice, token);

        } catch (Exception e) {
            System.err.println("Erro ao abrir mensagem: " + e.getMessage());
            mensagem(choice, token); // continua recursão mesmo com erro
        }
    }


    @Override
    public String toString() {
        return "to=" + to +
                ", from=" + from +
                ", message='" + message;
    }
}
