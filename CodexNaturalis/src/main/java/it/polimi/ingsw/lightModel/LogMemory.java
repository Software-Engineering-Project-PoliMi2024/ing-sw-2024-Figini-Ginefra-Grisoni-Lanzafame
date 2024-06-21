package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.utils.Observed;
import it.polimi.ingsw.utils.Observer;

import java.util.LinkedList;
import java.util.List;

public class LogMemory implements Observed {
    private final List<Observer> observers = new LinkedList<>();
    private final LinkedList<String> logMemory = new LinkedList<>();

    public void addLog(String log){
        logMemory.add(log);
        notifyObservers();
    }

    public LinkedList<String> getLogs() {
        return new LinkedList<>(logMemory);
    }

    public String getLastLog(){
        return logMemory.getLast();
    }

    public void clear(){
        logMemory.clear();
        notifyObservers();
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
