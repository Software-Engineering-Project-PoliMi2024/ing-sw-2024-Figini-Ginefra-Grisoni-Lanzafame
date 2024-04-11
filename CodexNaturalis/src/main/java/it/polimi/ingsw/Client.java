package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Controller controller;

        System.out.println("Hi there 👋!");
        System.out.println("Which interface would you prefer?");
        System.out.println("   [0] Textual");
        System.out.println("   [1] Graphical");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while(choice != 0 && choice != 1) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextInt();
        }

        if (choice == 0) {
            System.out.println("You chose the textual interface!");
        } else{
            System.out.println("You chose the graphical interface!");
        }

        System.out.println("Great choice! Let's move on! 🎉");

        System.out.println("Which communication protocol do you fancy today?🎩");

        System.out.println("   [0] Socket");
        System.out.println("   [1] RMI");

        choice = scanner.nextInt();

        while(choice != 0 && choice != 1) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextInt();
        }

        if (choice == 0) {
            System.out.println("You chose the Socket protocol!");
            controller = new SocketController();
            //TODO: View stuff

            System.out.println("Please enter your nickname:");
            String nickname = scanner.next();

            controller.connect("0.0.0.0", 4444, nickname);
        } else{
            System.out.println("You chose the RMI protocol!");
        }

        System.out.println("Great choice! Let's PLAY! 🎉🎉🎉");
    }
}
