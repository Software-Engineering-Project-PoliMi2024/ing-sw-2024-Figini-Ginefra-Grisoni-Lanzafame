package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class SecretObjectiveRenderable extends CardRenderable{
    public SecretObjectiveRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, museum, game, CardFace.FRONT, relatedCommands, view);
    }

    /**
     * Renders the secret objective card.
     */
    @Override
    public void render() {
        PromptStyle.printInABox("Secret Objective", CardTextStyle.getCardWidth() * 2);
        this.renderCard(this.getLightGame().getHand().getSecretObjective());
    }

    public void updateCommand(CommandPromptResult answer){
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_SECRET_OBJECTIVE:
                this.render();
                break;
            default:
                break;
        }
    }
}
