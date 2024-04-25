package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.PlaceAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.playerReleted.*;

import java.io.IOException;

public class PlaceMsg extends ActionMsg{
    final Placement placement;

    /**
     * The constructor of the class
     * @param placement made by the player
     */
    public PlaceMsg(Placement placement){
        super();
        this.placement = placement;
    }

    /**
     * @return the placement chose by the player
     */
    public Placement getPlacement() {
        return placement;
    }

    /**
     * update the model with the placement made by the user. Remove the card placed from the UserHand
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        ActionMsg.updateCodex(socketClientHandler, codex -> codex.playCard(placement));
        //placement.card in these case will always return a CardWithCorner witch
        // actually will be a CardInHand, because we are NOT working with a StartCard.
        //removeCard is a method from the UserHand so accept only CardInHand.
        ActionMsg.updateHand(socketClientHandler, hand -> {

            hand.removeCard((CardInHand) placement.card());

        });
        socketClientHandler.sendServerMessage(new PlaceAnswerMsg(this, placement));
    }
}
