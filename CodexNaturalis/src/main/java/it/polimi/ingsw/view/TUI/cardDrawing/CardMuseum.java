package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
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
    private final Map<Resource, Drawable> resourceBacks = new LinkedHashMap<>();

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
     * Gets all the card from the museum.
     * @return The cards in the museum indexed by their id.
     */
    public Map<Integer, TextCard> getCards(){
        return textCards;
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
     * @param resource The resource of the card.
     * @return The back of the card.
     */
    public Drawable getResourceBack(Resource resource){
        return resourceBacks.get(resource);
    }

    /**
     * Sets the back of a card based on its resource.
     * @param resource The resource of the card.
     * @param drawable The back of the card.
     */
    public void setResourceBack(Resource resource, Drawable drawable){
        resourceBacks.put(resource, drawable);
    }
}
