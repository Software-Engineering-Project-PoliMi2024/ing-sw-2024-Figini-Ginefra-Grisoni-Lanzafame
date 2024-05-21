package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class DrawCardForm extends FormRenderable{

    public DrawCardForm(String name, CommandPrompt[] commandPrompts, ControllerProvider view) {
        super(name, commandPrompts, view);
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        if(answer.getCommand() == CommandPrompt.DRAW_CARD){
            try{
                int deckId = Integer.parseInt(answer.getAnswer(0));
                int cardId = Integer.parseInt(answer.getAnswer(1));
                view.getController().draw(deckId == 0 ? DrawableCard.GOLDCARD : DrawableCard.RESOURCECARD, cardId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
