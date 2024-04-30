package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.view.TUI.observers.InputObserved;
import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputHandler implements InputObserved, Runnable, Serializable {
    private boolean keepGoing = true;
    private final List<InputObserver> observers = new ArrayList<>();

    public void attach(InputObserver observer) {
        observers.add(observer);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (keepGoing) {
            String input = scanner.nextLine();
            notifyObservers(input);
        }
    }

    public void stop() {
        keepGoing = false;
    }

    public void go(){
        keepGoing = true;
    }

    public void start() {
        Thread inputThread = new Thread(this);
        inputThread.start();
    }

    public void detach(InputObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String input) {
        for (InputObserver observer : observers) {
            observer.updateInput(input);
        }
    }
}
