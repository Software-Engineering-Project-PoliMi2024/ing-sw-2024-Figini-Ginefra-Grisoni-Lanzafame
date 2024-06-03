package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.model.playerReleted.PawnColors;

public class ChosePawnMsg extends ClientMsg{
    private final PawnColors color;

    public ChosePawnMsg(PawnColors color) {
        this.color = color;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().choosePawn(color);
    }
}
