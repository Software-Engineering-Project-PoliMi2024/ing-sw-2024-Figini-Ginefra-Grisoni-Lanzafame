package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;

import java.io.IOException;

public class DrawAnswerMsg extends AnswerMsg{
    final CardInHand card;

    /**
     * Initializes the answer message.
     *
     * @param parent The CommandMsg being answered.
     * @param card the card that was drawn
     */
    public DrawAnswerMsg(ActionMsg parent, CardInHand card) {
        super(parent);
        this.card = card;
    }

    /**
     * @return the card drawn
     */
    public CardInHand getCard() {
        return card;
    }

    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.update
        System.out.println(card + " was the card drawn");
    }
}
