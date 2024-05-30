package it.polimi.ingsw;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.TUI.TUI;

import java.io.File;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        checkOrCreateDataFolder();
        VirtualController controller;
        System.out.println("Hi there 👋!");


        Scanner scanner = new Scanner(System.in);

        System.out.println("Which interface would you prefer?");
        System.out.println("   [0] Textual");
        System.out.println("   [1] Graphical");

        String choice = scanner.nextLine();
        while(!choice.matches("[01]")) {
            System.out.println("Invalid choice! Please choose again.");
            choice = scanner.nextLine();
        }

        int choiceInt = Integer.parseInt(choice);

        if (choiceInt == 0) {
            System.out.println("You chose the textual interface!");
            new TUI().run();
        } else{
            System.out.println("You chose the graphical interface!");
            new GUI().run();
        }
    }

    private static void checkOrCreateDataFolder() {
        File dataFolder = new File(OSRelated.dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        // Create Cards folder
        File cardsFolder = new File(OSRelated.cardFolderDataPath);
        if (!cardsFolder.exists()) {
            cardsFolder.mkdirs();
        }
        // Create gameSaved folder
        File gameSavedFolder = new File(OSRelated.gameDataFolderPath);
        if (!gameSavedFolder.exists()) {
            gameSavedFolder.mkdirs();
        }
    }
}
