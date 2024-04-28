package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.HashMap;
import java.util.Map;

public class LightHand implements Differentiable {
    private LightCard secretObjective;
    private final Map<LightCard, Boolean> cardPlayability;
    private final LightCard[] cards;

    /**
     * The constructor of the class
     * */
    public LightHand(){
        this.cardPlayability = new HashMap<>();
        this.cards = new LightCard[3];
    }
    /**
     * Set the secret objective of the player
     * @param secretObjective the secret objective of the player
     */
    public void setSecretObjective(LightCard secretObjective) {
        this.secretObjective = secretObjective;
    }
    /**
     * @return the secret objective of the player
     */
    public LightCard getSecretObjective() {
        return secretObjective;
    }
    /**
     * @return the cards of the player
     */
    public Map<LightCard, Boolean> getCardPlayability() {
        return cardPlayability;
    }
    /**
     * @return the cards of the player
     */
    public LightCard[] getCards() {
        return cards;
    }
    /**
     * @param card to check
     * @return if the card is playable
     */
    public Boolean isPlayble(LightCard card){
        return cardPlayability.get(card);
    }
    /**
     * Add a card to the cards array and the cardPlayability Map
     * @param card that need to be added
     * @param playability is a boolean that specified if the card is playable
     * @throws IllegalCallerException if the player has already enough card
     */
    public void addCard(LightCard card, Boolean playability){
        if(length(cards) == 3){
            throw new IllegalCallerException();
        }else{
            for(int i=0; i<cards.length; i++){
                if(cards[i]==null){
                    cards[i]=card;
                    cardPlayability.put(card, playability);
                    break;
                }
            }
        }
    }
    private int length(LightCard[] arr){
        int i=0;
        for(LightCard r: arr){
            if(r!=null){
                i++;
            }
        }
        return i;
    }
    /**
     * Remove a lightCard from the cards array and the cardPlayability map
     * @param card that need to be removed
     */
    public void removeCard(LightCard card){
        boolean found = false;
        for(int i=0; i<cards.length && !found; i++){
            if(cards[i].equals(card)){
                cards[i] = null;
                cardPlayability.remove(card);
                found=true;
            }
        }
        if(!found){
            throw new IllegalArgumentException();
        }
    }
}
