package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Controller controller;
        View view;
        System.out.println("Hi there 👋!");
        System.out.println("Which communication protocol do you fancy today?🎩");
        System.out.println("   [0] Socket");
        System.out.println("   [1] RMI");

        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        while(choice != 0 && choice != 1) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextInt();
        }

        if (choice == 0) {
            System.out.println("You chose the Socket protocol!");
            controller = new SocketController();
        } else{
            System.out.println("You chose the RMI protocol!");
            controller = null;
        }

        System.out.println("Great choice! Let's move on! 🎉");

        System.out.println("Which interface would you prefer?");
        System.out.println("   [0] Textual");
        System.out.println("   [1] Graphical");

        choice = scanner.nextInt();
        while(choice != 0 && choice != 1) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextInt();
        }

        if (choice == 0) {
            System.out.println("You chose the textual interface!");
            view = new TUI(controller);
        } else{
            System.out.println("You chose the graphical interface!");
            view = null;
        }
        //view.transitionTo(CONNECT_FORM);
        System.out.println("Please enter your nickname(This will be done by the view_Connect_From):");
        String nickname = scanner.next();
        System.out.println("Great choice! Let's PLAY! 🎉🎉🎉");
        controller.connect("0.0.0.0", 4444, nickname, view, controller);
    }
}
