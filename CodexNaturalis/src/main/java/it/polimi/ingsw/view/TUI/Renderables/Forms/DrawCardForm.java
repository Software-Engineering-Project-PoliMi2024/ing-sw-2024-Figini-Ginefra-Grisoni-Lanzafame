package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that represents a draw card form. It allows the user to draw a card from a deck.
 */
public class DrawCardForm extends FormRenderable{
    /**
     * Creates a new DrawCardForm.
     * @param name The name of the renderable.
     * @param commandPrompts The commands related to this renderable.
     * @param view The controller provider.
     */
    public DrawCardForm(String name, CommandPrompt[] commandPrompts, ControllerProvider view) {
        super(name, commandPrompts, view);
    }

    /**
     * Updates the renderable with the answer to the command.
     * @param answer The answer to the command.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        if(answer.getCommand() == CommandPrompt.DRAW_CARD){
            try{
                int deckId = Integer.parseInt(answer.getAnswer(0));
                int cardId = Integer.parseInt(answer.getAnswer(1));
                view.getController().draw(deckId == 0 ? DrawableCard.GOLDCARD : DrawableCard.RESOURCECARD, cardId);
            }catch (Exception e){
                Configs.printStackTrace(e);
            }
        }
    }
}
