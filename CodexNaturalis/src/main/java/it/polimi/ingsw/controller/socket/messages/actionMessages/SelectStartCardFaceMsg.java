package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.SelectStartCardFaceAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.io.IOException;

public class SelectStartCardFaceMsg extends ActionMsg{
    final Position startiCardPosition = new Position(0,0);

    final StartCard startCard;
    final CardFace cardFace;

    /**
     * The constructor of the class
     * @param startCard chose by the user
     * @param cardFace chose by the user
     */
    public SelectStartCardFaceMsg(StartCard startCard, CardFace cardFace){
        super();
        this.startCard = startCard;
        this.cardFace = cardFace;
    }

    /**
     * @return the startCard chose by the user
     */
    public StartCard getStartCard() {
        return startCard;
    }

    /**
     * @return the cardFace chose by the player
     */
    public CardFace getCardFace() {
        return cardFace;
    }

    /**
     * @return the StartCardPosition(0,0)
     */
    public Position getStartiCardPosition() {
        return startiCardPosition;
    }

    /**
     *
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        socketClientHandler.getUser().getUserCodex().playCard(new Placement(startiCardPosition, startCard, cardFace));
        System.out.println(socketClientHandler.getUser().getNickname() + " chose " + cardFace + "as the face for the startCard");
        socketClientHandler.sendServerMessage(new SelectStartCardFaceAnswerMsg(this, startCard, cardFace));
    }
}
