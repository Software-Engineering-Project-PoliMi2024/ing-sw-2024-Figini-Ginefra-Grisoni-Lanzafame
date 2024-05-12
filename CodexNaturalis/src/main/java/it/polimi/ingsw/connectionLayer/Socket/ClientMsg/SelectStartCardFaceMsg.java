package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class SelectStartCardFaceMsg extends ClientMsg {
    private CardFace cardFace;

    public SelectStartCardFaceMsg(CardFace cardFace) {
        this.cardFace = cardFace;
    }

    public CardFace getCardFace() {
        return cardFace;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().selectStartCardFace(cardFace);
    }
}