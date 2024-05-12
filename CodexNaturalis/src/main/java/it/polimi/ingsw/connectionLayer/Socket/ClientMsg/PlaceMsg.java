package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class PlaceMsg extends ClientMsg {
    private LightPlacement placement;

    public PlaceMsg(LightPlacement placement) {
        this.placement = placement;
    }

    public LightPlacement getPlacement() {
        return placement;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().place(placement);
    }
}