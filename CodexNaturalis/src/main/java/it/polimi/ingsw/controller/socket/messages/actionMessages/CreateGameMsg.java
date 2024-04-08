package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.CreateGameAnswerMsg;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateGameMsg extends ActionMsg{
    private final String name;
    private final int numberOfPlayers;
    public CreateGameMsg(String name, int numberOfPlayers)
    {
        super();

        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void processMessage(SocketClientHandler socketClientHandler) throws IOException {
        Game game = new Game(name, numberOfPlayers);

        if(!socketClientHandler.getGames().addGame(game)){
            socketClientHandler.sendServerMessage(new CreateGameAnswerMsg(this, name, CreateGameAnswerMsg.Status.ERROR));
            return;
        };

        System.out.println("Game created: " + name + " with " + numberOfPlayers + " players.");
        System.out.println("Game list: " + Arrays.stream(socketClientHandler.getGames().getGameNames()).collect(Collectors.joining(", ")));

        socketClientHandler.sendServerMessage(new CreateGameAnswerMsg(this, name, CreateGameAnswerMsg.Status.OK));

    }
}
