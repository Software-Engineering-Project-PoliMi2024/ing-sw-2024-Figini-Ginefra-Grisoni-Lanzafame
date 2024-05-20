package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CanvasRenderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that can render the hand of the main player.
 */
public class HandRenderable extends CanvasRenderable {
    private final CardMuseum museum;
    private final LightGame lightGame;

    /**
     * Creates a new HandRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public HandRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, CardTextStyle.getCardWidth() * 3 + 3, CardTextStyle.getCardHeight(), relatedCommands, view);
        this.museum = museum;
        this.lightGame = game;
    }

    private void renderCard(LightCard card){
        TextCard textCard = museum.get(card.idFront());
        Drawable drawable = textCard.get(CardFace.FRONT);
        Printer.print(drawable.toString());
    }

    /**
     * Renders the secret objective card.
     */
    public void renderSecretObjective(){
        PromptStyle.printInABox("Secret Objective", CardTextStyle.getCardWidth() * 2);
        this.renderCard(lightGame.getHand().getSecretObjective());
    }

    /**
     * Renders the hand of the main player.
     */
    @Override
    public void render() {
        PromptStyle.printInABox("Hand", CardTextStyle.getCardWidth() * 3);
        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());
        for (int i = 0; i < 3; i++) {
            String cardNumber = new DecoratedString("[" + (i + 1) + "]", StringStyle.BOLD).toString();
            String text = "Card " + cardNumber;

            LightCard card = lightGame.getHand().getCards()[i];
            if(card == null){
                continue;
            }

            PromptStyle.printInABox(text, CardTextStyle.getCardWidth() * 2);
            this.canvas.draw(museum.get(card.idFront()).get(CardFace.FRONT), CardTextStyle.getCardWidth() /2 + i * (CardTextStyle.getCardWidth() + 1), CardTextStyle.getCardHeight() /2);
        }

        super.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    public void updateCommand(CommandPromptResult answer){
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_HAND:
                int cardIndex = Integer.parseInt(answer.getAnswer(0));
                this.render();
                break;
            case CommandPrompt.DISPLAY_SECRET_OBJECTIVE:
                this.renderSecretObjective();
                break;
            default:
                break;
        }
    }
}
