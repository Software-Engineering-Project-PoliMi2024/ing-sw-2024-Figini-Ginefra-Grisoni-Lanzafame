package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.LightGameRenderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

/**
 * This class is a Renderable that can render a card.
 */
public class CardRenderable extends LightGameRenderable {
    /** The face of the card to render. */
    private CardFace face;

    /** The card museum to use. */
    private final CardMuseum museum;

    /**
     * Creates a new CardRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param face The face of the card to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public CardRenderable(String name, CardMuseum museum, LightGame game, CardFace face, CommandPrompt[] relatedCommands, ControllerProvider view){
        super(name, relatedCommands, game, view);
        this.face = face;
        this.museum = museum;
    }

    /**
     * Renders a card by getting the drawable from the card museum and printing it.
     * @param card The card to render.
     */
    protected void renderCard(LightCard card){
        TextCard textCard = museum.get(card.idFront());
        Drawable drawable = textCard.get(this.face);
        Printer.print(drawable.toString());
    }

    /**
     * Renders the card. In this case it does nothing.
     */
    @Override
    public void render() {
        return;
    }

    /**
     * Updates the renderable based on the Terminal input. In this case it does nothing.
     * @param input The input.
     */
    public void updateInput(String input) {
        // Do nothing
    }

    /**
     * Sets the face of the card to render.
     * @param face The face of the card to render.
     */
    public void setFace(CardFace face) {
        this.face = face;
    }

    /**
     * Gets the face of the card to render.
     * @return The face of the card to render.
     */
    protected CardFace getFace() {
        return face;
    }

    /**
     * Gets the card museum.
     * @return The card museum.
     */
    protected CardMuseum getMuseum() {
        return museum;
    }

}
