package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.playerReleted.ChatMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the chat in the light model. It contains only the message that this user is allowed to see.
 */
public class LightChat implements Differentiable, Observed{
    /** The list of all the messages received by the user */
    private final LinkedList<ChatMessage> chatHistory;
    /** The list of all the observers of the chat */
    private final List<Observer> observers = new LinkedList<>();

    /**
     * The constructor of the class
     */
    public LightChat(){
        this.chatHistory = new LinkedList<>();
    }

    /**
     * Returns a copy of the chat history
     * @return a list of all the chatMessage received by the user
     */
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
