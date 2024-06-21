package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;

import java.io.Serializable;

public class Player implements Serializable {
    private final String nickname;
    private final Codex userCodex;
    private final Hand userHand;
    private PawnColors pawnColor = null;
    private PlayerState playerState = PlayerState.CHOOSE_START_CARD;

    public Player(String nickname){
        this.nickname = nickname;
        this.userCodex = new Codex();
        this.userHand = new Hand();
    }

    public PlayerState getState() {
        return playerState;
    }

    public void setState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setPawnColor(PawnColors pawnColor){
        this.pawnColor = pawnColor;
    }

    public PawnColors getPawnColor(){
        return this.pawnColor;
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
     * This method returns the number of cards in the user's hand
     * @return the number of cards in the user's hand
     */
    public synchronized int getHandSize(){
        return userHand.getHandSize();
    }

    public synchronized void placeStartCard(Placement placement){
        if(placement.position().x()!=0 || placement.position().y() != 0){
            throw new IllegalCallerException("The card provided must be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().setStartCard(null);
    }
    public synchronized void setSecretObjective(ObjectiveCard objectiveCard){
        userHand.setSecretObjective(objectiveCard);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && ((Player) obj).nickname.equals(nickname);
    }
}
