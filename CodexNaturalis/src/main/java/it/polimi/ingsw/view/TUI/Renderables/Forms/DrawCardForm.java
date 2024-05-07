package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class DrawCardForm extends FormRenderable{

    public DrawCardForm(String name, CommandPrompt[] commandPrompts, ControllerInterface controller) {
        super(name, commandPrompts, controller);
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        if(answer.getCommand() == CommandPrompt.DRAW_CARD){
            try{
                int deckId = Integer.parseInt(answer.getAnswer(0));
                int cardId = Integer.parseInt(answer.getAnswer(1));
                controller.draw(deckId == 0 ? DrawableCard.GOLDCARD : DrawableCard.RESOURCECARD, cardId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
