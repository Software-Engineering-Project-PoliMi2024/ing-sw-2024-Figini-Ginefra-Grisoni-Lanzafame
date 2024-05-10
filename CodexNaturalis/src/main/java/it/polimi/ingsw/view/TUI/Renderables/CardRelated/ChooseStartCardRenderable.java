package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.CardRenderable;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that can render a card and prompt the player to choose the face of the start card.
 */
public class ChooseStartCardRenderable extends CardRenderable {
    /**
     * Creates a new ChooseStartCardRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public ChooseStartCardRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view){
        super(name, museum, game, null, relatedCommands, view);
    }

    /**
     * Renders the card and prompts the player to choose the face of the start card.
     */
    @Override
    public void render() {
        LightCard card = this.getLightGame().getHand().getCards()[0];
        this.renderCard(card);
    }

    /**
     * Sets the renderable as active and prints the game start message.
     * @param active The active status.
     */
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            Printer.println(PromptStyle.GameStart);
        }
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.CHOOSE_START_SIDE:
                try {
                    CardFace face = answer.getAnswer(0).equals("front") ? CardFace.FRONT : CardFace.BACK;
                    view.getController().selectStartCardFace(face);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CommandPrompt.DISPLAY_START_FRONT:
                this.setFace(CardFace.FRONT);
                this.render();
                break;

            case CommandPrompt.DISPLAY_START_BACK:
                this.setFace(CardFace.BACK);
                this.render();
                break;
            default:
                break;
        }
    }
}
