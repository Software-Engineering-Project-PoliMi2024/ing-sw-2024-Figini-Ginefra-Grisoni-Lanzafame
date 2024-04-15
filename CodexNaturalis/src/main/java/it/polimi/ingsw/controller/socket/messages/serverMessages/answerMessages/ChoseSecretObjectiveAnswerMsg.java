package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;

import java.io.IOException;

public class ChoseSecretObjectiveAnswerMsg extends AnswerMsg{

    ObjectiveCard objectiveCard;

    /**
     * The constuctor of the class
     * @param parent The CommandMsg being answered.
     * @param objectiveCard chose by the player
     */
    public ChoseSecretObjectiveAnswerMsg(ActionMsg parent, ObjectiveCard objectiveCard){
        super(parent);
        this.objectiveCard = objectiveCard;
    }

    /**
     * Update the view
     * @param socketServerHandler who received an answer/notification from the server
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.transitionTO(UserDisplay)
        System.out.println("Set " + objectiveCard + " as secretObj on the server");
    }
}
