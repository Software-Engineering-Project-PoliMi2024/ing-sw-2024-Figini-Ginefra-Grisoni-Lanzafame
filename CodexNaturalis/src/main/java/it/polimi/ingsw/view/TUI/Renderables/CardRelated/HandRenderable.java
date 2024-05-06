package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that can render the hand of the main player.
 */
public class HandRenderable extends CardRenderable {

    /**
     * Creates a new HandRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param controller The controller to interact with.
     */
    public HandRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, museum, game, CardFace.FRONT, relatedCommands, controller);
    }

    /**
     * Renders the secret objective card.
     */
    public void renderSecretObjective(){
        PromptStyle.printInABox("Secret Objective", CardTextStyle.getCardWidth() * 2);
        this.renderCard(getLightGame().getHand().getSecretObjective());
    }

    /**
     * Renders the hand of the main player.
     */
    @Override
    public void render() {
        PromptStyle.printInABox("Hand - " + this.getFace().toString(), CardTextStyle.getCardWidth() * 3);
        for (int i = 0; i < 3; i++) {
            String cardNumber = new DecoratedString("[" + (i + 1) + "]", StringStyle.BOLD).toString();
            String text = "Card " + cardNumber;

            LightCard card = getLightGame().getHand().getCards()[i];
            if(card == null){
                continue;
            }

            if(this.getFace() == CardFace.FRONT && !getLightGame().getHand().isPlayble(card)){
                text = new DecoratedString(text, StringStyle.STRIKETHROUGH).toString();
            }

            PromptStyle.printInABox(text, CardTextStyle.getCardWidth() * 2);
            this.renderCard(card);
        }
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    public void updateCommand(CommandPromptResult answer){
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_HAND_FRONT:
                this.setFace(CardFace.FRONT);
                this.render();
                break;
            case CommandPrompt.DISPLAY_HAND_BACK:
                this.setFace(CardFace.BACK);
                this.render();
                break;
            case CommandPrompt.DISPLAY_SECRET_OBJECTIVE:
                this.setFace(CardFace.FRONT);
                this.renderSecretObjective();
                break;
            default:
                break;
        }
    }
}
