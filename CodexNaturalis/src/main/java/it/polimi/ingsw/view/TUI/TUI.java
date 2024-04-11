package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class TUI extends View{
    public TUI(Controller controller){
        super(controller);
    }

    public void run() {
        Thread inputThread = new Thread(TUI::inputScanner);
        inputThread.start();
    }
    public static void inputScanner() {
        Scanner scanner = new Scanner(System.in);
        while (controllerContinueRunning()) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                //controllerSend();
            }
        }
        scanner.close();
    }


    public static Boolean controllerContinueRunning(){return Boolean.FALSE;}

    public List<User> getUserInGame(){
        return null;
    }
}
