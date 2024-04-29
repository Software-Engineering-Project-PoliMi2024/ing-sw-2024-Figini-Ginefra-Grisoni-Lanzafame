package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class LightHandOthers {
    private final Resource[] cards;
    public LightHandOthers(){
        cards = new Resource[3];
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
