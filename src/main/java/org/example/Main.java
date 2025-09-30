package org.example;

import org.bson.Document;
import org.example.configuration.MongoHandler;
import org.example.entities.Message;
import org.example.entities.User;

import java.util.List;
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

        User user = new User(name.toLowerCase(), email, password);

        MongoHandler.insert("user", user.toDocument());
    }

    public static void registerMessage() throws Exception {
        System.out.println("Envio de Mensagem");
        scanner.nextLine();
        System.out.print("to: ");
        String to = scanner.nextLine();
        Document userTo = MongoHandler.findUser(to);

        System.out.print("from: ");
        String from = scanner.nextLine();
        Document userFrom = MongoHandler.findUser(from);

        System.out.print("message: ");
        String message = scanner.nextLine();

        System.out.print("token: ");
        String token = scanner.nextLine();

        Message msg = new Message(User.toUser(userTo), User.toUser(userFrom), message, token);

        MongoHandler.insert("message", msg.toDocument());
    }

    public static void listMessages() throws Exception {
        System.out.println("Lista de Mensagens");
        scanner.nextLine();

        System.out.print("Digite o Token: ");
        String token = scanner.nextLine();

        List<Document> messages = MongoHandler.findAll("message", token);
        
        if (messages.isEmpty()) throw new Exception("Nunhuma mesagem encontrada");

        messages.forEach(System.out::println);
    }

    public static void main(String[] args) {
        System.out.println("Escolha uma opção: \n1 - Cadastrar um usuário" +
                "\n2 - Enviar uma mensagem" +
                "\n3 - Listar suas mensagens" +
                "\n0 - Sair");
        System.out.print("Opção: ");
        int option = scanner.nextInt();

        while (option != 0) {
            switch (option) {
                case 1:
                    try {
                        registerUser();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        registerMessage();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        listMessages();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Erro");

            }
            System.out.println("Escolha uma opção: \n1 - Cadastrar um usuário" +
                    "\n2 - Enviar uma mensagem" +
                    "\n3 - Listar suas mensagens" +
                    "\n0 - Sair");
            System.out.print("Opção: ");
            option = scanner.nextInt();
        }

       MongoHandler.close();
    }
}
