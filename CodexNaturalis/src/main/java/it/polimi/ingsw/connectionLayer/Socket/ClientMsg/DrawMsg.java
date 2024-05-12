package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class DrawMsg extends ClientMsg {
    private DrawableCard deckID;
    private int cardID;

    public DrawMsg(DrawableCard deckID, int cardID) {
        this.deckID = deckID;
        this.cardID = cardID;
    }

    public DrawableCard getDeckID() {
        return deckID;
    }

    public int getCardID() {
        return cardID;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().draw(deckID, cardID);
    }
}