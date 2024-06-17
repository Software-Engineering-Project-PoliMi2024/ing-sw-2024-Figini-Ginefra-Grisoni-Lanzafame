package it.polimi.ingsw.controller.PublicModelClass;

import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameParty;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public record PublicGame(Game game) {

    /**
     * add a given objective card as the first public objective in Game
     *
     * @param obj the objectiveCard to add to the Game public objectives
     */
    public void setPublicObj(ObjectiveCard obj) {
        try {
            Field commonObjectiveField = game.getClass().getDeclaredField("commonObjective");
            commonObjectiveField.setAccessible(true);
            List<ObjectiveCard> objectiveCards = (List<ObjectiveCard>) commonObjectiveField.get(game);
            objectiveCards.removeFirst();
            objectiveCards.addFirst(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getLastTurnCounter(){
        try{
            Field lastTurnCounterField = game.getClass().getDeclaredField("lastTurnsCounter");
            lastTurnCounterField.setAccessible(true);
            return  (int) lastTurnCounterField.get(game);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
