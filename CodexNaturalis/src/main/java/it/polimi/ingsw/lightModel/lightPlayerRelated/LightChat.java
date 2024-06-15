package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.designPatterns.Observed;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.util.LinkedList;
import java.util.List;

public class LightChat implements Differentiable, Observed{
    private final LinkedList<ChatMessage> chatHistory;
    private final List<Observer> observers = new LinkedList<>();

    public LightChat(){
        this.chatHistory = new LinkedList<>();
    }

    public LinkedList<ChatMessage> getChatHistory() {
        return new LinkedList<>(chatHistory);
    }

    public void addMessage(ChatMessage message){
        this.chatHistory.add(message);
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        this.observers.add(observer);
        observer.update();
    }

    @Override
    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observers)
            o.update();
    }
}
