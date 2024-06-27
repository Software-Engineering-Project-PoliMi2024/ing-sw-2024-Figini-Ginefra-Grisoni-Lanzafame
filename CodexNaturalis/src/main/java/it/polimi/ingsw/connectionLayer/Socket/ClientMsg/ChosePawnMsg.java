package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.model.playerReleted.PawnColors;

/**
 * Message that contains the color of the pawn chosen by the player
 */
public class ChosePawnMsg extends ClientMsg{
    /**
     * The color of the pawn
     */
    private final PawnColors color;

    /**
     * Constructor
     * @param color the color of the pawn
     */
    public ChosePawnMsg(PawnColors color) {
        this.color = color;
    }

    /**
     * Give to the controller the choose color of the pawn
     * @param clientHandler the clientHandler that will process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().choosePawn(color);
    }
}
