package it.polimi.ingsw.controller.socket.messages.actionMessages;
import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.ChoseSecretObjectiveAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import java.io.IOException;

public class ChoseSecretObjectiveMsg extends ActionMsg{

    ObjectiveCard secrecretObjectiveCard;

    /**
     * The constructor of the class
     * @param secrecretObjectiveCard the card chose by the player
     */
    public ChoseSecretObjectiveMsg(ObjectiveCard secrecretObjectiveCard){
        this.secrecretObjectiveCard = secrecretObjectiveCard;
    }

    public ObjectiveCard getChoseSecretObjective(){
        return secrecretObjectiveCard;
    }

    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        socketClientHandler.getUser().getUserHand().setSecretObjective(secrecretObjectiveCard);
        socketClientHandler.sendServerMessage(new ChoseSecretObjectiveAnswerMsg(this, secrecretObjectiveCard));
        //testing
        System.out.println("Set " + socketClientHandler.getUser().getUserHand() + " as secretOBJ");
    }
}
