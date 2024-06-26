package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class PlaceMsg extends ClientMsg {
    /**
     * The lightPlacement of the card
     */
    private final LightPlacement placement;

    /**
     * Constructor
     * @param placement the lightPlacement of the card
     */
    public PlaceMsg(LightPlacement placement) {
        this.placement = placement;
    }

    /**
     * Call the controller for placing a card
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().place(placement);
    }
}