package it.polimi.ingsw.lightModel;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.Placement;

import java.io.Serializable;

public class Heavifier implements Serializable {

    public static ObjectiveCard heavifyObjectCard(LightCard lightCard, CardTable cardTable) {
        return cardTable.getCardLookUpObjective().lookUp(lightCard.idFront());
    }

    public static StartCard heavifyStartCard(LightCard lightCard, CardTable cardTable){
        return cardTable.getCardLookUpStartCard().lookUp(lightCard.idFront());
    }

    public static CardInHand heavifyCardInHand(LightCard lightCard, CardTable cardTable){
        if(cardTable.getCardLookUpGoldCard().isPresent(lightCard.idFront())){
            return cardTable.getCardLookUpGoldCard().lookUp(lightCard.idFront());
        }else{
            return cardTable.getCardLookUpResourceCard().lookUp(lightCard.idFront());
        }
    }

    public static Placement heavify(LightPlacement lightPlacement, CardTable cardTable){
        return new Placement(lightPlacement.position(), heavifyCardInHand(lightPlacement.card(), cardTable), lightPlacement.face());
    }

    public static Placement heavifyStartCardPlacement(LightPlacement lightPlacement, CardTable cardTable){
        return new Placement(lightPlacement.position(), heavifyStartCard(lightPlacement.card(), cardTable), lightPlacement.face());
    }
}
