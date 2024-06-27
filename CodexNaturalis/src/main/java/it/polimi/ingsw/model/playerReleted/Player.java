package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;

import java.io.Serializable;

/**
 * This class represents a player in the game.
 * It contains the player's nickname,
 * the player's codex,
 * the player's hand,
 * the player's pawn color
 * and the player's state.
 */
public class Player implements Serializable {
    /** The player's nickname */
    private final String nickname;
    /** The player's codex */
    private final Codex userCodex;
    /** The player's hand */
    private final Hand userHand;
    /** The player chosen pawn color */
    private PawnColors pawnColor = null;
    /** The player's state */
    private PlayerState playerState = PlayerState.CHOOSE_START_CARD;

    /**
     * This constructor creates a player with the given nickname
     * @param nickname the player's nickname
     */
    public Player(String nickname){
        this.nickname = nickname;
        this.userCodex = new Codex();
        this.userHand = new Hand();
    }

    /** @return the player's state */
    public PlayerState getState() {
        return playerState;
    }

    /** @param playerState the player's state to set */
    public void setState(PlayerState playerState) {
        this.playerState = playerState;
    }

    /** @param pawnColor the player's pawn color to set */
    public void setPawnColor(PawnColors pawnColor){
        this.pawnColor = pawnColor;
    }

    /** @return the player's pawn color */
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

    /**
     * This method plays a card in the user's codex
     * it checks if the card is a valid card to play if not it throws an exception
     * adds the card to the user's codex and removes it from the user's hand
     * @param placement the placement of the card to play
     */
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

    /**
     * This method places the start card in the user's codex
     * it checks if the card is a valid start card if not it throws an exception
     * adds the card to the user's codex and removes it from the user's hand
     * @param placement the placement of the start card
     */
    public synchronized void placeStartCard(Placement placement){
        if(placement.position().x()!=0 || placement.position().y() != 0){
            throw new IllegalCallerException("The card provided must be a startCard");
        }
        this.userCodex.playCard(placement);
        this.getUserHand().setStartCard(null);
    }
    /** @param objectiveCard the objective card to set */
    public synchronized void setSecretObjective(ObjectiveCard objectiveCard){
        userHand.setSecretObjective(objectiveCard);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && ((Player) obj).nickname.equals(nickname);
    }
}
