package org.example.entities;

import org.bson.Document;
import org.example.configuration.Criptografia;
import org.example.configuration.MongoHandler;

import java.util.Scanner;

public class User {

    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password);
    }

    public static User toUser(Document doc) {
        if (doc == null) return null;

        User user = new User();
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));

        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void registerUser() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Cadastro de Usuário");
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User(name.toLowerCase(), email, password);

        MongoHandler.insert("user", user.toDocument());
    }
}
