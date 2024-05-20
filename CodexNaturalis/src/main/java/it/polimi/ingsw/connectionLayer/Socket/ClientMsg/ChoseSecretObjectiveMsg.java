package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class ChoseSecretObjectiveMsg extends ClientMsg {
    private LightCard secretObjective;

    public ChoseSecretObjectiveMsg(LightCard secretObjectiveCard) {
        this.secretObjective = secretObjectiveCard;
    }

    public LightCard getSecretObjectiveCard() {
        return secretObjective;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().choseSecretObjective(secretObjective);
    }
}