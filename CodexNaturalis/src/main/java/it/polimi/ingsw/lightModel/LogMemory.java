package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to store all the logs that are sent to the client
 */
public class LogMemory implements Observed {
    /** a list of Observer that are observing this class */
    private final List<Observer> observers = new LinkedList<>();
    /**A list of all logs sent to the client*/
    private final LinkedList<String> logMemory = new LinkedList<>();

    /**
     * Add a log to the logMemory and notify all the observers
     * @param log the log to add
     */
    public void addLog(String log){
        logMemory.add(log);
        notifyObservers();
    }

    /**
     * @return the last log added to the logMemory
     */
    public String getLastLog(){
        return logMemory.getLast();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }
}
