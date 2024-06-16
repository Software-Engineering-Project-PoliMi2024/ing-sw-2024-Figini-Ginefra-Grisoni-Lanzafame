package it.polimi.ingsw.controller.PublicModelClass;

import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.tableReleted.Game;

import java.lang.reflect.Field;
import java.util.List;

public class PublicGame {
    public final Game game;

    public PublicGame(Game game){
        this.game = game;
    }

    /**
     * add a given objective card as the first public objective in Game
     * @param obj the objectiveCard to add to the Game public objectives
     */
    public void setPublicObj(ObjectiveCard obj){
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
}
