package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.model.playerReleted.Placement;

import java.io.IOException;

public class PlaceAnswerMsg extends AnswerMsg{

    final Placement placement;
    /**
     * Initializes the answer message.
     * @param parent The CommandMsg being answered.
     * @param placement made by the player
     */
    public PlaceAnswerMsg(ActionMsg parent, Placement placement) {
        super(parent);
        this.placement = placement;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.update()
        System.out.println(placement +" was added to your Codex");
    }
}
