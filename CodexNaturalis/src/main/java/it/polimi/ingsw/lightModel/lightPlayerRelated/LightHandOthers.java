package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class LightHandOthers implements Differentiable {
    private final Resource[] cards;
    public LightHandOthers(){
        cards = new Resource[2];
    }
    public LightHandOthers(Resource[] cards){
        if(cards.length > 3)
            throw new IllegalArgumentException();
        else
            this.cards = cards;
    }
    public Resource[] getCards() {
        return cards;
    }
    private int length(Resource[] arr){
        int i=0;
        for(Resource r: arr){
            if(r!=null){
                i++;
            }
        }
        return i;
    }
    public void addCard(Resource card){
        if(length(cards) == 3){
            throw new IllegalCallerException();
        }else{
            for(int i=0; i<cards.length; i++){
                if(cards[i]==null){
                    cards[i]=card;
                }
            }
        }
    }
    public void removeCard(Resource card){
        boolean found = false;
        for(int i=0; i<cards.length && !found; i++){
            if(cards[i].equals(card)){
                cards[i] = null;
                found=true;
            }
        }
        if(!found){
            throw new IllegalArgumentException();
        }
    }
}
