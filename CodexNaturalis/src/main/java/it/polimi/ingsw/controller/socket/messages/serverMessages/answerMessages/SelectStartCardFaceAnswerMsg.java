package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

import java.io.IOException;

public class SelectStartCardFaceAnswerMsg extends AnswerMsg{
    StartCard startCard;
    CardFace cardFace;
    /**
     * Initializes the answer message.
     * @param parent The CommandMsg being answered.
     * @param startCard chose by the user
     * @param cardFace chose by the user
     */
    public SelectStartCardFaceAnswerMsg(ActionMsg parent, StartCard startCard, CardFace cardFace) {
        super(parent);
        this.startCard = startCard;
        this.cardFace = cardFace;
    }

    /**
     * Update the view
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.TransitioTo(UserDisplay)
        System.out.println(startCard + " placed with the " + cardFace +" being displayed");
    }
}
