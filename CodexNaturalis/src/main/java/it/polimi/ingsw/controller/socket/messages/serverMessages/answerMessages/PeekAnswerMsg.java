package it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;

import java.io.IOException;

public class PeekAnswerMsg extends AnswerMsg{

    private final Codex peekedCodex;
    private final Hand peekedHand;

    private final String nickname;
    /**
     * Initializes a PeekAnswerMsg object with the provided parameters.
     * @param parent    The CommandMsg object being answered.
     * @param peekedCodex The Codex from the player being peeked.
     * @param peekedHand  The Hand from the player being peeked.
     * @param nickname The nickname of the player being peeked
     */
    public PeekAnswerMsg(ActionMsg parent, Codex peekedCodex, Hand peekedHand, String nickname) {
        super(parent);
        this.peekedCodex=peekedCodex;
        this.peekedHand=peekedHand;
        this.nickname = nickname;
    }

    public Codex getPeekedCodex() {
        return peekedCodex;
    }

    public Hand getPeekedHand() {
        return peekedHand;
    }

    public String getNickname() {
        return nickname;
    }
    @Override
    public void processMessage(SocketServerHandler socketServerHandler) throws IOException {
        //view.update
        System.out.println("peeked element of " + nickname + " are:");
        System.out.println(peekedCodex);
        System.out.println(peekedHand);
    }
}
