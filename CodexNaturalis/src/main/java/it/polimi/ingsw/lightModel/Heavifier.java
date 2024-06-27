package it.polimi.ingsw.lightModel;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.Placement;

import java.io.Serializable;

/**
 * This class is used to convert LightObject present in the LightModel to the corresponding Object present in the Model
 */
public class Heavifier implements Serializable {

    /**
     * Given a lightCard, it searches for the ID in the CardLookUpObjective in the cardTable and returns the ObjectiveCard
     * @param lightCard the lightCard to be heavified
     * @param cardTable the cardTable containing all the CardLookUp objects
     * @return the ObjectiveCard corresponding to the lightCard
     */
    public static ObjectiveCard heavifyObjectCard(LightCard lightCard, CardTable cardTable) {
        return cardTable.getCardLookUpObjective().lookUp(lightCard.idFront());
    }

    /**
     * Given a lightCard, it searches for the ID in the CardLookUpStartCard in the cardTable and returns the StartCard
     * @param lightCard the lightCard to be heavified
     * @param cardTable the cardTable containing all the CardLookUp objects
     * @return the StartCard corresponding to the lightCard
     */
    public static StartCard heavifyStartCard(LightCard lightCard, CardTable cardTable){
        return cardTable.getCardLookUpStartCard().lookUp(lightCard.idFront());
    }

    /**
     * Given a lightCard, it searches for the ID in the CardLookUpGoldCard or in the CardLookUpResourceCard in the cardTable and returns it
     * @param lightCard the lightCard to be heavified
     * @param cardTable the cardTable containing all the CardLookUp objects
     * @return the GoldCard corresponding to the lightCard
     */
    public static CardInHand heavifyCardInHand(LightCard lightCard, CardTable cardTable){
        if(cardTable.getCardLookUpGoldCard().isPresent(lightCard.idFront())){
            return cardTable.getCardLookUpGoldCard().lookUp(lightCard.idFront());
        }else{
            return cardTable.getCardLookUpResourceCard().lookUp(lightCard.idFront());
        }
    }

    /**
     * Given a lightPlacement containing a CardInHand, it heavifies it
     * @param lightPlacement the lightPlacement to be heavified
     * @param cardTable the cardTable containing all the CardLookUp objects
     * @return the heavified Placement
     */
    public static Placement heavify(LightPlacement lightPlacement, CardTable cardTable){
        return new Placement(lightPlacement.position(), heavifyCardInHand(lightPlacement.card(), cardTable), lightPlacement.face());
    }

    /**
     * Given a lightPlacement containing a StartCard, it heavifies it
     * @param lightPlacement the lightPlacement to be heavified
     * @param cardTable the cardTable containing all the CardLookUp objects
     * @return the heavified Placement
     */
    public static Placement heavifyStartCardPlacement(LightPlacement lightPlacement, CardTable cardTable){
        return new Placement(lightPlacement.position(), heavifyStartCard(lightPlacement.card(), cardTable), lightPlacement.face());
    }
}
