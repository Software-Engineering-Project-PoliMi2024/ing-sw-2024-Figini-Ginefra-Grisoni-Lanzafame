package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

public class PlaceCardForm extends FormRenderable{
    LightGame game;
    public PlaceCardForm(String name, LightGame game, CommandPrompt[] commandPrompts, ControllerInterface controller){
        super(name, commandPrompts, controller);
        this.game = game;
    }

    public void updateCommand(CommandPromptResult answer){
        if(answer.getCommand() == CommandPrompt.PLACE_CARD){
            try {
                int cardIndex = Integer.parseInt(answer.getAnswer(0)) - 1;
                CardFace face = Integer.parseInt(answer.getAnswer(1)) == 0 ? CardFace.FRONT : CardFace.BACK;
                int frontierIndex = Integer.parseInt(answer.getAnswer(2));

                LightCard card = game.getHand().getCards()[cardIndex];

                LightFrontier frontier = game.getMyCodex().getFrontier();
                List<Position> positions = frontier.frontier();

                if(frontierIndex < frontier.size() && (game.getHand().isPlayble(card) || face == CardFace.BACK)){
                    LightPlacement placement = new LightPlacement(positions.get(frontierIndex), card, face);
                    this.controller.place(placement);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
