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

    public User(User other){
        this.nickname = other.nickname;
        this.userCodex = new Codex(other.userCodex);
        this.userHand = new Hand(other.userHand);
    }

    /** @return the user nickname*/
    public String getNickname(){
        return this.nickname;
    }
    /** @return the user codex address*/
    public Codex getUserCodex(){
        return this.userCodex;
    }

    public void setUserCodex(Codex codex){
        this.userCodex = codex;
    }
    /** @return the user hand address*/
    public Hand getUserHand(){
        return this.userHand;
    }

    public void setUserHand(Hand hand){
        this.userHand=hand;
    }

    public void playCard(Placement placement){
        if(placement.position().x()==0 && placement.position().y() == 0){
            throw new IllegalCallerException("The card provided cannot be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().removeCard((CardInHand) placement.card()); //A user can place only a CardInHand, so the cast is safe
    }

    public void placeStartCard(Placement placement){
        if(placement.position().x()!=0 || placement.position().y() != 0){
            throw new IllegalCallerException("The card provided must be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().setStartCard(null);
    }
    public void setSecretObject(ObjectiveCard objectiveCard){
        userHand.setSecretObjective(objectiveCard);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).nickname.equals(nickname);
    }
}
