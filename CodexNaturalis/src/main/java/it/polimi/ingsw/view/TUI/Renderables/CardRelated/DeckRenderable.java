package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.utils.designPatterns.Observed;
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
 * This class is a Renderable that displays two decks with options to draw cards from them as well as the common objectives.
 * In order to render the card stacked horizontally it extends CanvasRenderable.
 */
public class DeckRenderable extends CanvasRenderable {
    /** The deck of gold card. */
    private final LightDeck goldDeck;
    /** The deck of resource cards. */
    private final LightDeck resourceDeck;
    /** The card museum containing the rendered cards*/
    private final CardMuseum museum;
    /** The light game */
    private final LightGame lightGame;

    /**
     * Creates a new DeckRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public DeckRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, CardTextStyle.getCardWidth() * 4 + 4, CardTextStyle.getCardHeight() * 2 + 1, relatedCommands, view);
        this.goldDeck = game.getGoldDeck();
        this.resourceDeck = game.getResourceDeck();

        this.museum = museum;
        this.lightGame = game;

        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());
    }

    /**
     * Renders the two decks composed of the buffer and the back of the next card to draw as well as the common objectives.
     * All are fetched from the light model.
     */
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
            if(card == null) {
                continue;
            }

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

    @Override
    public List<Observed> getObservedLightModel() {
        return List.of(lightGame.getGoldDeck(), lightGame.getResourceDeck());
    }
}
