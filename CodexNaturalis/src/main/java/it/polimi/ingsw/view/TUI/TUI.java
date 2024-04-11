package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class TUI extends View{
    public void run() {
        Thread inputThread = new Thread(TUI::inputScanner);
        inputThread.start();
    }
    public static void inputScanner() {
        Scanner scanner = new Scanner(System.in);
        while (controllerContinueRunning()) {
            System.out.print("Enter your input: ");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                controllerSend();
            }
        }
        scanner.close();
    }

    public static void controllerSend(){}
    /** the method in the controller that return true if it is needed to continue t check */
    public static Boolean controllerContinueRunning(){return Boolean.FALSE;}

    public List<User> getUserInGame(){
        return null;
    }
}
