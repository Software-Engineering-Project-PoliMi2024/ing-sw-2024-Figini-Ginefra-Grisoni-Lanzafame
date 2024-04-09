package it.polimi.ingsw.view;

import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.States.viewState;

import java.io.IOError;
import java.util.List;
import java.util.Scanner;

public class View {
    private final List<viewState> states = null;

    public static void run(){
        Thread inputThread = new Thread(View::inputScanner);
        inputThread.start();

    }
    public static void inputScanner() {
        Scanner scanner = new Scanner(System.in);
        while (controllerContinueRunning()) {
            System.out.print("Enter your input: ");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                controllerSend();
            }else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new IOError(e);
                }
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
