package org.example;

import org.example.configuration.MongoHandler;
import org.example.entities.Message;
import org.example.entities.User;

import java.util.Scanner;



public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Message message = new Message();

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
                        User.registerUser();
                        break;
                    case 2:
                        message.registerMessage();
                        break;
                    case 3:
                        message.listMessages();
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
