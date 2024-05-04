package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.CardRenderable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;

/**
 * This class is a Renderable that can render a card and prompt the player to choose between two objective cards.
 */
public class ChooseObjectiveCardRenderable extends CardRenderable {
    /**
     * Creates a new ChooseObjectiveCardRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param controller The controller to interact with.
     */
    public ChooseObjectiveCardRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, museum, game, CardFace.FRONT, relatedCommands, controller);
    }

    /**
     * Renders the two objective cards.
     */
    public void render(){
        String firstOptionNumber = new DecoratedString("[1]", StringStyle.BOLD).toString();
        PromptStyle.printInABox("Option " + firstOptionNumber, CardTextStyle.getCardWidth() * 2);

        LightCard firstOption = getLightGame().getHand().getCards()[0];
        this.renderCard(firstOption);

        String secondOptionNumber = new DecoratedString("[2]", StringStyle.BOLD).toString();
        PromptStyle.printInABox("Option " + secondOptionNumber, CardTextStyle.getCardWidth() * 2);

        LightCard secondOption = getLightGame().getHand().getCards()[1];
        this.renderCard(secondOption);
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param command The command prompt result.
     */
    public void updateCommand(CommandPromptResult command){
        switch (command.getCommand()) {
            case CommandPrompt.CHOOSE_OBJECTIVE_CARD:
                try {
                    int cardIndex = Integer.parseInt(command.getAnswer(0)) - 1;
                    controller.choseSecretObjective(getLightGame().getHand().getCards()[cardIndex]);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS:
                this.render();
                break;
            default:
                break;
        }
    }
}
