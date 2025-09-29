package org.example;

import org.bson.Document;
import org.example.configuration.MongoHandler;
import org.example.entities.Message;
import org.example.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void registerUser() {
        System.out.println("Cadastro de Usuário");
        scanner.nextLine();
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Document doc = new Document("name", name)
                .append("email", email)
                .append("password", password);

        MongoHandler.insert("user", doc);
    }

    public static void main(String[] args) {
        System.out.println("Escolha uma opção: \n1 - Cadastrar um usuário\n2 - Enviar uma mensagem\n3 - Listar suas mensagens");
        System.out.print("Opção: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                registerUser();
                break;
            case 2:

                break;
        }

       MongoHandler.close();
    }
}
