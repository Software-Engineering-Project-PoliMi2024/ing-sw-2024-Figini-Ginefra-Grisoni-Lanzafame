package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

/**
 * This class is a Renderable that represents a form to place a card.
 */
public class PlaceCardForm extends FormRenderable{
    /** The game to interact with. */
    LightGame game;

    /**
     * Creates a new PlaceCardForm.
     * @param name The name of the renderable.
     * @param game The game to interact with.
     * @param commandPrompts The commands related to this renderable.
     * @param view The controller provider.
     */
    public PlaceCardForm(String name, LightGame game, CommandPrompt[] commandPrompts, ControllerProvider view){
        super(name, commandPrompts, view);
        this.game = game;
    }

    /**
     * Updates the renderable with the answer to the command.
     * @param answer The answer to the command.
     */
    public void updateCommand(CommandPromptResult answer){
        if(answer.getCommand() == CommandPrompt.PLACE_CARD){
            try {
                int cardIndex = Integer.parseInt(answer.getAnswer(0)) - 1;
                CardFace face = Integer.parseInt(answer.getAnswer(1)) == 0 ? CardFace.FRONT : CardFace.BACK;
                int frontierIndex = Integer.parseInt(answer.getAnswer(2));

                LightCard card = game.getHand().getCards()[cardIndex];

                LightFrontier frontier = game.getMyCodex().getFrontier();
                List<Position> positions = frontier.frontier();

                if(frontierIndex < frontier.size() && (game.getHand().isPlayable(card) || face == CardFace.BACK)){
                    LightPlacement placement = new LightPlacement(positions.get(frontierIndex), card, face);
                    view.getController().place(placement);
                }
            } catch (Exception e) {
                Configs.printStackTrace(e);
            }
        }
    }
}
