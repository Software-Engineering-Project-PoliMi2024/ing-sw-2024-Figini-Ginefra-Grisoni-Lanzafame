package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.CreateGameAnswerMsg;
import it.polimi.ingsw.controller.socket.server.ClientHandler;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;

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
    public void processMessage(ClientHandler clientHandler) throws IOException {
        Game game = new Game(name, numberOfPlayers);

        clientHandler.setGame(game);
        clientHandler.addGame(game);

        System.out.println("Game created: " + name + " with " + numberOfPlayers + " players.");
        System.out.println("Game list: " + clientHandler.getGames());

        clientHandler.sendServerMessage(new CreateGameAnswerMsg(this, name, CreateGameAnswerMsg.Status.OK));

    }
}
