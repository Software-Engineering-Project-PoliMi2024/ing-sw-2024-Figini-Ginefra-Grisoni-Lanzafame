package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;

import java.io.Serializable;

public class User implements Serializable {
    private final String nickname;
    private Codex userCodex;
    private Hand userHand;

    public User(String nickname){
        this.nickname = nickname;
        this.userCodex = new Codex();
        this.userHand = new Hand();
    }

    /** @return the user nickname*/
    public String getNickname(){
        return this.nickname;
    }
    /** @return the user codex address*/
    public Codex getUserCodex(){
        return this.userCodex;
    }

    /** @return the user hand address*/
    public Hand getUserHand(){
        return this.userHand;
    }

    public synchronized void playCard(Placement placement){
        if(placement.position().x()==0 && placement.position().y() == 0){
            throw new IllegalCallerException("The card provided cannot be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().removeCard((CardInHand) placement.card()); //A user can place only a CardInHand, so the cast is safe
    }

    /**
     * This method checks if the user has already placed the start card
     * @return true if the user has already placed the start card, false otherwise
     */
    public synchronized boolean hasPlacedStartCard(){
        return (userCodex.getPlacementAt(new Position(0,0)) != null);
    }

    /**
     * This method returns the number of cards in the user's hand
     * @return the number of cards in the user's hand
     */
    public synchronized int getHandSize(){
        return userHand.getHandSize();
    }

    /**
     * This method checks if the user has already chosen the objective
     * @return true if the user has already chosen the objective, false otherwise
     */
    public synchronized boolean hasChosenObjective(){
        return userHand.getSecretObjective() != null;
    }

    public synchronized void placeStartCard(Placement placement){
        if(placement.position().x()!=0 || placement.position().y() != 0){
            throw new IllegalCallerException("The card provided must be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().setStartCard(null);
    }
    public synchronized void setSecretObject(ObjectiveCard objectiveCard){
        userHand.setSecretObjective(objectiveCard);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).nickname.equals(nickname);
    }
}
