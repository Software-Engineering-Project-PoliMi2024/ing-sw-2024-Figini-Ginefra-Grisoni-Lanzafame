package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.RMI.ControllerRMI;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.controller2.VirtualControllerRMI;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ControllerInterfaceClient controller;
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
            controller = null;
        } else{
            System.out.println("You chose the RMI protocol!");
            controller = new VirtualControllerRMI();
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
        view.transitionTo(ViewState.SERVER_CONNECTION);
        view.run();
    }
}
