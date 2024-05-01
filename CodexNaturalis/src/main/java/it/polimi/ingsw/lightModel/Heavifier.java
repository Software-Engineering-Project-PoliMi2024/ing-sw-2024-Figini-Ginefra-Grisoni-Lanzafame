package it.polimi.ingsw.lightModel;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.io.Serializable;

public class Heavifier implements Serializable {

    public static ObjectiveCard heavifyObjectCard(LightCard lightCard, MultiGame games) {
       return games.getCardLookUpObjective().lookUp(lightCard.id());
    }

    public static StartCard heavifyStartCard(LightCard lightCard, MultiGame games){
        return games.getCardLookUpStartCard().lookUp(lightCard.id());
    }

    public static CardInHand heavifyCardInHand(LightCard lightCard, MultiGame games){
        if(games.getCardLookUpGoldCard().isPresent(lightCard.id())){
            return games.getCardLookUpGoldCard().lookUp(lightCard.id());
        }else{
            return games.getCardLookUpResourceCard().lookUp(lightCard.id());
        }
    }

    public static Placement heavify(LightPlacement lightPlacement, MultiGame games){
        return new Placement(lightPlacement.position(), heavifyCardInHand(lightPlacement.card(), games), lightPlacement.face());
    }
}