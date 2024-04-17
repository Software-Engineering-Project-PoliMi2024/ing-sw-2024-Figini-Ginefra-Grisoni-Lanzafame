package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.NetworkMessage;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameParty;

import java.io.IOException;

public abstract class ActionMsg extends NetworkMessage {
    /**
     * An abstract method for all ActionMsg. Each ActionMsg must send an AnswerMsg to the client
     * who performed an Action
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    public abstract void processMessage(SocketClientHandler socketClientHandler) throws IOException;


    public interface codexAction {
        void performAction(Codex codex);
    }

    public static void updateCodex(SocketClientHandler socketClientHandler, codexAction action) {
        //socketClientHandler.getUser() return a "copy" of User. IDC if that it isn't the real obj/address
        User user = socketClientHandler.getUser();
        //From that user I get a Codex. Again IDC if that is the real deal
        Codex codex = user.getUserCodex();

        // Perform the action passed as argument
        action.performAction(codex);

        // Update the codex in the user object and set it back into the SocketClientHandler
        user.setUserCodex(codex);
        socketClientHandler.setUser(user);
    }

    public interface handAction {
        void performAction(Hand hand);
    }

    public static void updateHand(SocketClientHandler socketClientHandler, handAction action) {
        //socketClientHandler.getUser() return a "copy" of User. IDC if that it isn't the real obj/address
        User user = socketClientHandler.getUser();
        //From that user I get a Hand. Again IDC if that is the real deal
        Hand hand = user.getUserHand();

        // Perform the action passed as argument
        action.performAction(hand);

        // Update the hand in the user object and set it back into the SocketClientHandler
        user.setUserHand(hand);
        socketClientHandler.setUser(user);
    }

    public interface multiGameAction {
        void performAction(MultiGame games);
    }

    public static void updateMultiGame(SocketClientHandler socketClientHandler, multiGameAction action) {
        MultiGame games = socketClientHandler.getGames();
        action.performAction(games);
        socketClientHandler.setGames(games);
    }

    public interface gameAction {
        void performAction(Game game);
    }

    public static void updateGame(SocketClientHandler socketClientHandler, gameAction action) {
        Game game = socketClientHandler.getGame();
        action.performAction(game);
        socketClientHandler.setGame(game);
    }

    public interface gamePartyAction {
        void performAction(GameParty gameParty);
    }

    public static void updateGameParty(SocketClientHandler socketClientHandler, gamePartyAction action) {
        Game game = socketClientHandler.getGame();
        GameParty gameParty = game.getGameParty();

        action.performAction(gameParty);

        game.setGameParty(gameParty);
        socketClientHandler.setGame(game);
    }
}
