package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CanvasRenderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a Renderable that displays two decks with options to draw cards from them.
 */
public class DeckRenderable extends CanvasRenderable {
    private final LightDeck goldDeck;
    private final LightDeck resourceDeck;
    private final CardMuseum museum;
    private final LightGame lightGame;

    public DeckRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, CardTextStyle.getCardWidth() * 4 + 4, CardTextStyle.getCardHeight() * 2 + 1, relatedCommands, view);
        this.goldDeck = game.getGoldDeck();
        this.resourceDeck = game.getResourceDeck();

        this.museum = museum;
        this.lightGame = game;

        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());
    }

    @Override
    public void render() {
        Printable upperPrintable = new Printable("");

        PromptStyle.printInABox(upperPrintable, "Gold Deck ", CardTextStyle.getCardWidth() * 6 + 2);

        List<Printable> resourceLabels = new LinkedList<>();
        renderDeck(resourceLabels, goldDeck, 0);

        List<Printable> goldLabels = new LinkedList<>();
        renderDeck(goldLabels, resourceDeck, 1);

        Printable printedResourceLabels = new Printable("");
        Printer.printStackedHorizontally(printedResourceLabels, resourceLabels, "  ");

        Printable CommonObjectiveLabel = new Printable("");
        PromptStyle.printInABox(CommonObjectiveLabel, "Common Objectives", CardTextStyle.getCardWidth() * 2 - 2);

        Printer.printStackedHorizontally(upperPrintable, List.of(printedResourceLabels, CommonObjectiveLabel), "  ");

        Printer.print(upperPrintable);

        renderCommonObjectives(resourceLabels);

        // Render the canvas
        super.render();

        Printer.printStackedHorizontally(goldLabels, "  ");
        PromptStyle.printInABox("Resource Deck ", CardTextStyle.getCardWidth() * 6 + 4);
    }

    private void renderCommonObjectives(List<Printable> labels){
        LightCard[] commonObjectives = lightGame.getPublicObjective();

        for(int i = 0; i < commonObjectives.length; i++) {
            LightCard card = commonObjectives[i];
            Drawable drawableCard = museum.get(card.idFront()).get(CardFace.FRONT);
            this.canvas.draw( drawableCard, CardTextStyle.getCardWidth() /2 + 3 * (CardTextStyle.getCardWidth() + 1) + 1, CardTextStyle.getCardHeight() /2 + i * (CardTextStyle.getCardHeight() + 1));
        }

    }

    private void renderDeck(List<Printable> labels, LightDeck deck, int row) {
        for (int i = 0; i < 2; i++) {  //  first two cards  visible
            LightCard card = deck.getCardBuffer()[i];
            if (card == null) {
                continue;
            }
            String cardNumber = new DecoratedString("[" + i + "]", StringStyle.BOLD).toString();
            String text = "Card " + cardNumber;

            Printable resourceLabel = new Printable("");
            PromptStyle.printInABox(resourceLabel, text, CardTextStyle.getCardWidth() * 2 - 2);
            labels.add(resourceLabel);

            Drawable drawableCard = museum.get(card.idFront()).get(CardFace.FRONT);
            this.canvas.draw(drawableCard,
                    CardTextStyle.getCardWidth() / 2 + i * (CardTextStyle.getCardWidth() + 1),
                    CardTextStyle.getCardHeight() / 2 + row * (CardTextStyle.getCardHeight() + 1)
            );
        }

        // Render the top invisible card resource only, showing resource back
        if (deck.getDeckBack() != null) {

            Printable resourceLabel = new Printable("");
            PromptStyle.printInABox(resourceLabel, "Next Draw" , CardTextStyle.getCardWidth() * 2 - 2);
            labels.add(resourceLabel);

            Drawable resourceBack = museum.getBackFromId(deck.getDeckBack().idBack());
            this.canvas.draw(resourceBack,
                    CardTextStyle.getCardWidth() / 2 + 2 * (CardTextStyle.getCardWidth() + 1),
                    CardTextStyle.getCardHeight() / 2 + row * (CardTextStyle.getCardHeight() + 1)
            );
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
