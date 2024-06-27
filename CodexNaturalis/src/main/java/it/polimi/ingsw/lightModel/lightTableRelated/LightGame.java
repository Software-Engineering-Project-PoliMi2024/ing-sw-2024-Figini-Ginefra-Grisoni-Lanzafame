package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ViewState;

import java.util.*;

/**
 * This class represents the game seen by the view.
 * It contains all the information that the view needs to render the game.
 */
public class LightGame implements Differentiable {
    /** The lightGameParty for this game*/
    private LightGameParty lightGameParty;
    /** The map of the codexes bound to each player */
    private Map<String, LightCodex> codexMap;
    /**The hand of the client */
    private LightHand hand;
    /** The hands of the other players */
    private Map<String, LightHandOthers> handOthers;
    /** A map containing a ResourceCard lightDeck and a GoldCard lightDeck */
    private Map<DrawableCard, LightDeck> decks;
    /** The public objectives of the game. Each element of this array is null until the publicObjective are drawn*/
    private LightCard[] publicObjective;
    /** The list of winners of the game. This list is empty until the game ends*/
    private List<String> winners;

    /**
     * Constructor of the class
     * Initialize the lightGameParty, the codexMap, the hand,
     * the handOthers, the decks, the publicObjective and the winners
     */
    public LightGame(){
        this.lightGameParty = new LightGameParty();
        this.codexMap = new HashMap<>();
        this.hand = new LightHand();
        this.handOthers = new HashMap<>();
        this.decks = new HashMap<>();
        decks.put(DrawableCard.RESOURCECARD, new LightDeck());
        decks.put(DrawableCard.GOLDCARD, new LightDeck());
        publicObjective = new LightCard[2];
        this.winners = new ArrayList<>();
    }

    /**
     * Add a public objective to the game in the first empty slot of the publicObjective array
     * Notify the lightDeck observers of the update
     * @param objective the lightCard of the public objective to add
     */
    public void addObjective(LightCard objective){
        System.out.println("Adding public objective");
        for(int i = 0; i<publicObjective.length; i++){
            if(publicObjective[i] == null){
                publicObjective[i] = objective;
                break;
            }
        }
        //notify the view about the new public objective
        decks.forEach((drawableCard, lightDeck) -> lightDeck.notifyObservers());
    }

    /**
     * @return the pointer to the publicObjective array
     */
    public LightCard[] getPublicObjective() {
        return publicObjective;
    }

    /**
     * @return the pointer to the LightHand object
     */
    public LightHand getHand() {
        return hand;
    }

    /** @return the pointer to the lightGameParty object */
    public LightGameParty getLightGameParty() {
        return lightGameParty;
    }

    /**
     * @return the pointer to the handOthers map
     */
    public Map<String, LightHandOthers> getHandOthers() {
        return handOthers;
    }

    /**
     * @return the pointer to the decks map
     */
    public Map<DrawableCard, LightDeck> getDecks() {
        return decks;
    }

    /**
     * @return the pointer to the codexMap map
     */
    public Map<String, LightCodex> getCodexMap() {
        return codexMap;
    }

    /**
     * Update the other of another player. If the player is not in the handOthers map, it adds him
     * @param owner the nickname of the owner of the hand
     * @param card the lightBack of the card to add
     */
    public void addOtherHand(String owner, LightBack card){
        if(this.handOthers.get(owner) == null){
            this.handOthers.put(owner, new LightHandOthers());
        }else{
            this.handOthers.get(owner).addCard(card);
        }
    }

    /**
     Update the lightGame by adding each player received as parameter.
     Populate the codexMap by putting the player's nickname as key and a new LightCodex as value.
     If the player is not the client, populate the handOthers map by putting the player's nickname as key and a new LightHandOthers as value.
     */
    public void gameInitialization(List<String> owners){
        for(String owner : owners){
            this.codexMap.put(owner, new LightCodex());
            if(!owner.equals(lightGameParty.getYourName()))
                this.handOthers.put(owner, new LightHandOthers());
        }
    }
    /** return the ResourceDeck */
    public LightDeck getResourceDeck(){
        return decks.get(DrawableCard.RESOURCECARD);
    }

    /** return the GoldDeck */
    public LightDeck getGoldDeck(){
        return decks.get(DrawableCard.GOLDCARD);
    }

    /**
     * Update the lightDeck (type) by changing the top card of the deck of the type received as parameter
     * @param back the lightBack of the card that need to be set as topDeck
     * @param type the type of the deck that need to be updated
     */
    public void setTopDeck(LightBack back, DrawableCard type){
        decks.get(type).setTopDeckCard(back);
    }

    /**
     * Update the hand by changing the playability of the card received as parameter
     * @param card the card to update
     * @param playability the new playability of such card
     */
    public void updateHandPlayability(LightCard card, Boolean playability){
        this.hand.updatePlayability(card, playability);
    }

    /**
     * Update the lightDeck (type) by substituting a card in the buffer with a new one
     * @param card the new card in the buffer to add
     * @param position the position where to add the new card and remove the old one (0 or 1)
     */
    public void setDeckBuffer(LightCard card, DrawableCard type,Integer position){
        decks.get(type).substituteBufferCard(card, position);
    }

    /**
     * Update the lightGameParty by setting the gameName
     * @param gameName the name of the game
     */
    public void setGameName(String gameName) {
        this.lightGameParty.setGameName(gameName);
    }

    /**
     * Update the lightGameParty by setting player as active
     * @param player the nickname of the player to be set as active
     */
    public void setActivePlayer(String player){
        this.lightGameParty.setActivePlayer(player);
    }

    /**
     * Update the lightGameParty by setting player as inactive
     * @param player the nickname of the player to be set as inactive
     */
    public void setInactivePlayer(String player){
        this.lightGameParty.setInactivePlayer(player);
    }

    /**
     * Update the lightGameParty by setting the player as the current one
     * @param player the nickname of the player to be set as current
     */
    public void setCurrentPlayer(String player){
        this.lightGameParty.setCurrentPlayer(player);
    }

    /**
     * Update the hand by adding a secretObjective to it
     * @param secretObjective the secretObjective to add
     */
    public void setSecretObjective(LightCard secretObjective){
        this.hand.setSecretObjective(secretObjective);
    }

    /**
     * Update the hand by adding a card to it
     * @param card the lightCard to add
     * @param playability the playability of the card
     */
    public void addCard(LightCard card, Boolean playability){
        this.hand.addCard(card, playability);
    }

    /**
     * Update the hand by removing a card from it
     * @param card the lightCard to remove
     */
    public void removeCard(LightCard card){
        this.hand.removeCard(card);
    }

    /**
     * Update the lightCodex of the player by updating the points
     * @param points the new points of the player
     * @param nickname the nickname of the owner of the codex
     */
    public void setPoint(int points, String nickname){
        codexMap.get(nickname).setPoints(points);
    }

    /**
     * Update the lightGameParty by setting the first player
     * @param firstPlayer the nick of the first player
     */
    public void setFirstPlayerName(String firstPlayer){
        lightGameParty.setFirstPlayerName(firstPlayer);
    }

    /**
     * Update the lightGameParty by setting the pawnColor chosen by the player
     * @param nickname the nick of the player who chose the color
     * @param color the PawnColor chosen by the player
     */
    public void addPlayerColor(String nickname, PawnColors color){
        lightGameParty.addPlayerColor(nickname, color);
    }

    /**
     * Update the lightGameParty by setting the pawnChoices
     * @param pawnColorsList the list of pawnColors to set
     */
    public void setPawnChoices(List<PawnColors> pawnColorsList){
        lightGameParty.setPawnChoices(pawnColorsList);
    }

    /**
     * Entirely remove a player from the game.
     * Remove his codex and his hand from the maps.
     * Also remove him from the lightGameParty
     * @param nickname the nickname of the player to remove
     */
    public void removePlayer(String nickname){
        codexMap.remove(nickname);
        handOthers.remove(nickname);
        lightGameParty.removePlayer(nickname);
    }

    /**
     * Update the codex of the player by updating his frontier
     * @param lightFrontier the new frontier of the player
     * @param nickname the nickname of the player
     */
    public void setFrontier(LightFrontier lightFrontier, String nickname){
        codexMap.get(nickname).setFrontier(lightFrontier);
    }

    /**
     * Update the codex of the player by updating his collectables
     * @param collectables the new collectables of the player
     * @param nickname the nickname of the player
     */
    public void setCollectable(Map<Collectable, Integer> collectables, String nickname){
        codexMap.get(nickname).setCollectables(collectables);
    }

    /**Return the nickname of the current player */
    public String getCurrentPlayer(){
        return lightGameParty.getCurrentPlayer();
    }

    /**Set the nickname of the client in the lightGameParty */
    public void setYourName(String yourName){
        this.lightGameParty.setYourName(yourName);
    }

    /**Reset the lightGame by creating new instances of all the objects
     * Create a new instance of: lightGameParty, codexMap, hand, handOthers, decks and winners
     */
    public void reset(){
        this.lightGameParty = new LightGameParty();
        this.codexMap = new HashMap<>();
        this.hand = new LightHand();
        this.handOthers = new HashMap<>();
        this.decks = new HashMap<>();
        this.winners = new ArrayList<>();
        decks.put(DrawableCard.RESOURCECARD, new LightDeck());
        decks.put(DrawableCard.GOLDCARD, new LightDeck());
        publicObjective = new LightCard[2];
    }

    /**Return the codex of the client*/
    public LightCodex getMyCodex(){
        return codexMap.get(lightGameParty.getYourName());
    }

    /** Populate the winner list with the winners of the game*/
    public void setWinners(List<String> winners){
        this.winners.addAll(winners);
    }

    /**Return the list of winners of the game */
    public List<String> getWinners() {
        return new ArrayList<>(winners);
    }
}
