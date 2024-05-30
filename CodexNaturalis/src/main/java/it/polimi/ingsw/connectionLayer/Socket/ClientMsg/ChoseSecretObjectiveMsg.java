package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class ChoseSecretObjectiveMsg extends ClientMsg {
    private LightCard secretObjective;

    public ChoseSecretObjectiveMsg(LightCard secretObjectiveCard) {
        this.secretObjective = secretObjectiveCard;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().chooseSecretObjective(secretObjective);
    }
}