package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is a museum of cards. It stores the rendered design of all the cards indexed by their id.
 * It also stores the basic back of a card based on its permanent resource.
 */
public class CardMuseum implements Serializable {
    /** The cards stored in the museum. */
    private final Map<Integer, TextCard> textCards;

    /** The backs of the cards based on the resource. */
    private final Map<Integer, Drawable> idBacks = new LinkedHashMap<>();

    /**
     * Creates a new CardMuseum.
     */
    public CardMuseum() {
        textCards = new LinkedHashMap<>();
    }

    /**
     * Adds a card to the museum.
     * @param id The id of the card.
     * @param card The card to add.
     */
    public void set(int id, TextCard card){
        textCards.put(id, card);
    }

    /**
     * Gets a card from the museum.
     * @param id The id of the card.
     * @return The card.
     */
    public TextCard get(int id){
        return textCards.get(id);
    }

    /**
     * Gets the number of cards in the museum.
     * @return The number of cards in the museum.
     */
    public int getSize(){
        return textCards.size();
    }

    /**
     * Gets the back of a card based on its resource.
     * @param backId The id that identify the back of the card.
     * @return The back of the card.
     */
    public Drawable getBackFromId(int backId){
        return idBacks.get(backId);
    }

    /**
     * Sets the back of a card based on its resource.
     * @param backId The id that identify the back of the card.
     * @param drawable The back of the card.
     */
    public void setBackFromId(int backId, Drawable drawable){
        idBacks.put(backId, drawable);
    }
}
