package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.view.TUI.observers.InputObserved;
import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is an input handler. It reads input from the console and notifies the observers.
 */
public class InputHandler implements InputObserved, Runnable, Serializable {
    /** Whether the input handler should keep going. */
    private boolean keepGoing = true;

    /** The observers of the input handler. */
    private final List<InputObserver> observers = new ArrayList<>();

    /**
     * Attaches an observer to the input handler.
     * @param observer The observer to attach.
     */
    public void attach(InputObserver observer) {
        observers.add(observer);
    }

    /**
     * Runs the input handler.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (keepGoing) {
            String input = scanner.nextLine();
            notifyObservers(input);
        }
    }

    /**
     * Stops the input handler.
     */
    public void stop() {
        keepGoing = false;
    }

    /**
     * Makes the input handler go.
     */
    public void go(){
        keepGoing = true;
    }

    /**
     * Starts the input handler in a new thread.
     */
    public void start() {
        Thread inputThread = new Thread(this);
        inputThread.start();
    }

    /**
     * Detaches an observer from the input handler.
     * @param observer The observer to detach.
     */
    public void detach(InputObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies the observers of the input handler.
     * @param input The input to notify.
     */
    public void notifyObservers(String input) {
        for (InputObserver observer : observers) {
            observer.updateInput(input);
        }
    }
}
