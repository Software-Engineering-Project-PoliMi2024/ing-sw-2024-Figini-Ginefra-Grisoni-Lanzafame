package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHand;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.*;

public class LightGame implements Differentiable {
    private LightGameParty lightGameParty;
    private Map<String, LightCodex> codexMap;
    private LightHand hand;
    private Map<String, LightHandOthers> handOthers;
    private Map<DrawableCard, LightDeck> decks;
    private LightCard[] publicObjective;

    public LightGame(){
        this.lightGameParty = new LightGameParty();
        this.codexMap = new HashMap<>();
        this.hand = new LightHand();
        this.handOthers = new HashMap<>();
        this.decks = new HashMap<>();
        decks.put(DrawableCard.RESOURCECARD, new LightDeck());
        decks.put(DrawableCard.GOLDCARD, new LightDeck());
        publicObjective = new LightCard[2];
    }
    public void addObjective(LightCard objective){
        for(int i = 0; i<publicObjective.length; i++){
            if(publicObjective[i] == null){
                publicObjective[i] = objective;
                return;
            }
        }
    }
    public LightCard[] getPublicObjective() {
        return publicObjective;
    }

    public LightHand getHand() {
        return hand;
    }

    public LightGameParty getLightGameParty() {
        return lightGameParty;
    }

    public Map<String, LightHandOthers> getHandOthers() {
        return handOthers;
    }

    public Map<DrawableCard, LightDeck> getDecks() {
        return decks;
    }

    public Map<String, LightCodex> getCodexMap() {
        return codexMap;
    }
    public void setCodexMap(Map<String, LightCodex> codexMap) {
        for(String player : codexMap.keySet()){
            this.codexMap.put(player, codexMap.get(player));
        }
    }
    public void setCodexMap(String owner, LightCodex codex){
        this.codexMap.put(owner, codex);
    }
    public void addOtherHand(String owner, Resource card){
        this.handOthers.get(owner).addCard(card);
    }
    public void setOtherHand(Map<String, LightHandOthers> hands){
        for(String owner : hands.keySet()){
            this.handOthers.put(owner, hands.get(owner));
        }
    }
    public LightDeck getResourceDeck(){
        return decks.get(DrawableCard.RESOURCECARD);
    }
    public LightDeck getGoldDeck(){
        return decks.get(DrawableCard.GOLDCARD);
    }
    public void setResourceDeck(LightDeck deck){
        decks.put(DrawableCard.RESOURCECARD, deck);
    }
    public void setGoldDeck(LightDeck deck){
        decks.put(DrawableCard.GOLDCARD, deck);
    }
    public void setTopDeck(Resource resource, DrawableCard type){
        decks.get(type).setTopDeckCard(resource);
    }
    public void setDeckBuffer(LightCard card, DrawableCard type,Integer position){
        decks.get(type).substituteBufferCard(card, position);
    }
    public void setGameName(String gameName) {
        this.lightGameParty.setGameName(gameName);
    }
    public void setActivePlayer(String player){
        this.lightGameParty.setActivePlayer(player);
    }
    public void setInactivePlayer(String player){
        this.lightGameParty.setInactivePlayer(player);
    }
    public void setCurrentPlayer(String player){
        this.lightGameParty.setCurrentPlayer(player);
    }
    public void setSecretObjective(LightCard secretObjective){
        this.hand.setSecretObjective(secretObjective);
    }
    public void addCard(LightCard card, Boolean playability){
        this.hand.addCard(card, playability);
    }
    public void removeCard(LightCard card){
        this.hand.removeCard(card);
    }
    public void setPoint(int points, String nickname){
        if(codexMap.get(nickname) == null){
            codexMap.put(nickname, new LightCodex());
            codexMap.get(nickname).setPoints(points);
        }
    }

    public void setFrontier(LightFrontier lightFrontier, String nickname){
        codexMap.get(nickname).setFrontier(lightFrontier);
    }

    public void setCollectable(Map<Collectable, Integer> collectables, String nickname){
        codexMap.get(nickname).setCollectables(collectables);
    }
    public void setYourName(String yourName){
        this.lightGameParty.setYourName(yourName);
    }
    public void reset(){
        this.lightGameParty = new LightGameParty();
        this.codexMap = new HashMap<>();
        this.hand = new LightHand();
        this.handOthers = new HashMap<>();
        this.decks = new HashMap<>();
        decks.put(DrawableCard.RESOURCECARD, new LightDeck());
        decks.put(DrawableCard.GOLDCARD, new LightDeck());
        publicObjective = new LightCard[2];
    }
    public LightCodex getMyCodex(){
        return codexMap.get(lightGameParty.getYourName());
    }
}
