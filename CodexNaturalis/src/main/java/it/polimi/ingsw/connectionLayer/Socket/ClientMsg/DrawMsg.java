package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class DrawMsg extends ClientMsg {
    /**
     * The deck from which the player is drawing
     */
    private final DrawableCard deckID;

    /**
     * The id of the card that the player is drawing
     */
    private final int cardID;

    /**
     * Constructor
     * @param deckID the deck from which the player is drawing
     * @param cardID the id of the card that the player is drawing
     */
    public DrawMsg(DrawableCard deckID, int cardID) {
        this.deckID = deckID;
        this.cardID = cardID;
    }

    /**
     * Call the controller for drawing a card
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().draw(deckID, cardID);
    }
}