package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that displays two decks with options to draw cards from them.
 */
public class DeckRenderable extends CardRenderable {
    private final LightDeck goldDeck;
    private final LightDeck resourceDeck;

    public DeckRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, museum, game, CardFace.FRONT, relatedCommands, controller);
        this.goldDeck = game.getGoldDeck();
        this.resourceDeck = game.getResourceDeck();
    }

    @Override
    public void render() {
        PromptStyle.printInABox("Gold Deck ", CardTextStyle.getCardWidth() * 2);
        renderDeck(goldDeck);

        PromptStyle.printInABox("Resource Deck ", CardTextStyle.getCardWidth() * 2);
        renderDeck(resourceDeck);
    }

    private void renderDeck(LightDeck deck) {
        for (int i = 0; i < 2; i++) {  //  first two cards  visible
            LightCard card = deck.getCardBuffer()[i];
            if (card == null) {
                continue;
            }
            String cardNumber = new DecoratedString("[" + (i + 1) + "]", StringStyle.BOLD).toString();
            String text = "Card " + cardNumber;

            PromptStyle.printInABox(text, CardTextStyle.getCardWidth() * 2);
            this.renderCard(card);
        }
        // Render the top invisible card resource only, showing resource back
        if (deck.getCardDeck() != null) {
            String resourceBack = getMuseum().getResourceBack(deck.getCardDeck()).toString();
            PromptStyle.printInABox("Next Draw" , CardTextStyle.getCardWidth() * 3);
            Printer.println(  resourceBack );
        }
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_DECKS:
                this.render();
                break;
            default:
                break;
        }
    }
}
