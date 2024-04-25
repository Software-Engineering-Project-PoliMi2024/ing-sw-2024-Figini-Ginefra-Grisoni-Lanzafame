package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.DoubleModelDiff;
import it.polimi.ingsw.model.lightModel.diffs.DoubleModelDifferentiable;

import java.util.HashMap;
import java.util.Map;

public class LightHand implements DoubleModelDifferentiable<LightCard, Boolean> {
    private LightCard secretObjective;
    private final Map<LightCard, Boolean> cardPlayability;
    private final LightCard[] cards;

    /**
     * The constructor of the class
     */
    public LightHand(){
        this.cardPlayability = new HashMap<>();
        this.cards = new LightCard[3];
    }

    /**
     * @param secretObjective that need to be set
     */
    public void setSecretObjective(LightCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public LightCard getSecretObjective() {
        return secretObjective;
    }

    public Map<LightCard, Boolean> getCardPlayability() {
        return cardPlayability;
    }

    public LightCard[] getCards() {
        return cards;
    }


    public Boolean isPlayble(LightCard card){
        return cardPlayability.get(card);
    }

    /**
     * Add a card to the cards array and the cardPlayability Map
     * @param card that need to be added
     * @param playability is a boolean that specified if the card is playable
     * @throws IllegalCallerException if the player has already enough card
     */
    private void addCard(LightCard card, Boolean playability){
        if(cards.length == 3){
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

    /**
     * Remove a lightCard from the cards array and the cardPlayability map
     * @param card that need to be removed
     */
    private void removeCard(LightCard card){
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

    /**
     * Process a HandDiff adding all card in the addList of parameter A
     * with they playability in the addList in the parameter B
     * Remove all card from the removeList of parameter A
     * @param diff to process
     */
    @Override
    public void applyDiff(DoubleModelDiff<LightCard, Boolean> diff) {
        for(LightCard card : diff.A().getRemoveList()){
            removeCard(card);
        }
        for(int i=0; i<diff.A().getAddList().size(); i++){
            addCard(diff.A().getAddList().get(i), diff.B().getAddList().get(i));
        }
    }
}
