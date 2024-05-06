package it.polimi.ingsw;

import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionClientRMI;
import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ConnectionLayerClient controller;
        View view;
        System.out.println("Hi there 👋!");
        System.out.println("Which communication protocol do you fancy today?🎩");

        System.out.println("   [0] Socket");
        System.out.println("   [1] RMI");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        while(!choice.matches("[01]")) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextLine();
        }
        int choiceInt = Integer.parseInt(choice);

        if (choiceInt == 0) {
            System.out.println("You chose the Socket protocol!");
            controller = null;
        } else{
            System.out.println("You chose the RMI protocol!");
            controller = new ConnectionClientRMI();
        }

        System.out.println("Great choice! Let's move on! 🎉");

        System.out.println("Which interface would you prefer?");
        System.out.println("   [0] Textual");
        System.out.println("   [1] Graphical");

        choice = scanner.nextLine();
        while(!choice.matches("[01]")) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextLine();
        }
        choiceInt = Integer.parseInt(choice);

        if (choiceInt == 0) {
            System.out.println("You chose the textual interface!");
            view = new TUI(controller);
        } else{
            System.out.println("You chose the graphical interface!");
            view = null;
        }
        view.run();
    }
}
