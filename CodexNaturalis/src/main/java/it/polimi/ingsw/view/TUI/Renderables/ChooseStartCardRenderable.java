package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class ChooseStartCardRenderable extends CardRenderable{
    public ChooseStartCardRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, museum, game, null, relatedCommands, controller);
    }

    @Override
    public void render() {
        LightCard card = this.getLightGame().getHand().getCards()[0];
        this.renderCard(card);
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.CHOOSE_START_SIDE:
                try {
                    CardFace face = answer.getAnswer(0).equals("front") ? CardFace.FRONT : CardFace.BACK;
                    controller.selectStartCardFace(this.getLightGame().getHand().getCards()[0], face);
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
