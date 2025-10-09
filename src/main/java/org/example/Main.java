package org.example;

import org.bson.Document;
import org.example.configuration.Criptografia;
import org.example.configuration.MongoHandler;
import org.example.entities.Message;
import org.example.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    public static Criptografia criptografia = new Criptografia();

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

        System.out.print("title: ");
        String title = scanner.nextLine();

        System.out.print("message: ");
        String message = scanner.nextLine();

        System.out.print("token: ");
        String token = scanner.nextLine();

        Message msg = new Message(User.toUser(userTo), User.toUser(userFrom), title, message, criptografia.encrypt(token), LocalDate.now());

        MongoHandler.insert("message", msg.toDocument());
    }

    public static void listMessages() throws Exception {
        System.out.println("Lista de Mensagens");
        scanner.nextLine();

        System.out.print("Digite o Token: ");
        String token = scanner.nextLine();

        List<Document> messages = MongoHandler.findAll("message", criptografia.encrypt(token));

        if (messages.isEmpty()) throw new Exception("Nunhuma mesagem encontrada");

        // TODO: Implementar um jeito de escolher a mensagem e mostrar mostrar o conteudo dela
        messages.stream()
                .map(doc -> String.format("to=%s, from=%s, title=%s",
                        doc.getString("to"),
                        doc.getString("from"),
                        doc.getString("title")))
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        int option = -1;

        while (option != 0) {
            System.out.println("Escolha uma opção: \n1 - Cadastrar um usuário" +
                    "\n2 - Enviar uma mensagem" +
                    "\n3 - Listar suas mensagens" +
                    "\n0 - Sair");
            System.out.print("Opção: ");
            option = scanner.nextInt();

            try {
                switch (option) {
                    case 0:
                        break;
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        registerMessage();
                        break;
                    case 3:
                        listMessages();
                        break;
                    default:
                        throw new Exception("Opção invalida");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.println();
            }
        }

        MongoHandler.close();
    }
}
